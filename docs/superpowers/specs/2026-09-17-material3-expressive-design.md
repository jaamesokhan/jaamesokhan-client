# Material 3 Expressive upgrade

## Goal

Move the app's Compose theme from a hand-authored, static Material 3 color/shape system to the stock Material 3 Expressive design language: generated tonal color scheme, Expressive shape scale, Expressive motion, and Expressive component variants where a direct analog exists in this codebase.

Scope decision (confirmed with user): replace the custom design-system tokens (`Color.kt`, `Radius.kt`) with stock M3 Expressive defaults, rather than keeping the current brand-specific hex/radius scale and only adopting the Expressive engine underneath.

## Non-goals

- No change to `Dimens.kt` (spacing scale) — spacing is orthogonal to Material 3 Expressive, which concerns color, shape, typography, motion, and components, not a layout spacing system.
- No change to typography content — the app's Persian `CustomFonts` family stays mapped onto the existing 15 `Typography` roles. Font choice is a content requirement, not a design-system choice.
- No forced adoption of Expressive components that have no existing analog in this codebase (FAB menu, button groups, split buttons) — none of `FloatingActionButton`, `TabRow`, or `SegmentedButton` are currently used anywhere in the app.
- No restructuring of navigation/information architecture. `Navbar.kt` and `Topbar.kt` keep their current custom composable structure (floating pill bar, custom top bar) — only their color/shape/indicator styling changes.
- No dynamic (wallpaper-based) color. Static brand-seeded palette only, per user decision.

## Dependency changes

- `gradle/libs.versions.toml`: bump `composeBom` from `2024.09.03` to `2026.08.00` (brings stable `androidx.compose.material3:material3` 1.4.0 — Material 3 Expressive APIs are stable as of 1.4.0, no `@ExperimentalMaterial3ExpressiveApi` opt-ins required).
- Add new dependency `com.materialkolor:material-kolor:5.0.0` (Compose Multiplatform library, Android-compatible) for seed-color → tonal palette generation via `rememberDynamicColorScheme`.
- No AGP/Kotlin version change needed. The Compose compiler is wired via the `org.jetbrains.kotlin.plugin.compose` Gradle plugin (tied to the Kotlin version), decoupled from the Compose BOM, so bumping the BOM alone is safe.

## Color

`ui/theme/Theme.kt`:
- Delete the hand-built `lightScheme`/`darkScheme` (`lightColorScheme(...)`/`darkColorScheme(...)` with ~26 named parameters each).
- Replace with `rememberDynamicColorScheme(seedColor = BrandSeed, isDark = darkTheme, style = PaletteStyle.TonalSpot)`, called once per theme branch (light/dark). `PaletteStyle.TonalSpot` is the standard M3 algorithm — it keeps the seed hue dominant as `primary`. (Explicitly not `PaletteStyle.Expressive`, which is a MaterialKolor palette-generation mode that deliberately shifts hue away from the seed for a discordant/playful look — confirmed with user this is not wanted; "Material 3 Expressive" here refers to the design-language update, not that specific palette mode.)
- Seed constant: `val BrandSeed = Color(0xFF5B6642)` (the app's existing brand primary), defined in `Color.kt`.
- `dynamicColor` parameter on `JaamebaadeclientTheme` stays `false` by default — no Material You wallpaper theming.

`ui/theme/Color.kt`:
- Delete all `*Light`/`*Dark` hex constants that fed the manual schemes (`primaryLight`, `secondaryContainerDark`, etc. — the ~50 constants currently in the file).
- Keep `BrandSeed`.
- The custom extension color roles (`secondaryS30/S40/S50`, `neutralN70/N95/N100`, `primary20/90` in `Theme.kt`) get recomputed from the generated scheme's tonal palette (via MaterialKolor's palette accessors) instead of referencing hardcoded hex constants, so they stay consistent with whatever the seed produces.

## Shape

`ui/theme/Radius.kt`:
- Delete the file's custom scale (`radius4/6/10/12/15/17/20/30`) and the derived shapes (`ButtonShape`, `CardShape`, `ChipShape`, `SheetTopShape`, `PillShape`), along with its header comment ("Corner-radius scale from the Jaame Sokhan design system... keep exact, do not round") — this comment is superseded by the user's explicit choice to move to stock M3 Expressive shapes.
- `Theme.kt`'s `MaterialTheme(...)` call gets an explicit `shapes = Shapes(...)` using the M3 Expressive default scale: `extraSmall` (4dp), `small` (8dp), `medium` (12dp), `large` (16dp), `extraLarge` (28dp), plus the new Expressive tiers `largeIncreased`, `extraLargeIncreased`, `extraExtraLarge` left at M3's own stock defaults (not hand-tuned).
- All current consumers of the deleted shapes (21 files, found via `grep -rl "Radius\.\|ButtonShape\|CardShape\|ChipShape\|SheetTopShape\|PillShape"`) get updated to reference the matching `MaterialTheme.shapes.*` token instead (e.g. `CardShape` uses → `MaterialTheme.shapes.large`, `ButtonShape` uses → `MaterialTheme.shapes.medium`, `PillShape` uses → `CircleShape` or `MaterialTheme.shapes.extraExtraLarge` depending on call site — exact mapping decided per call site during implementation, matching visual intent).

## Motion

`ui/theme/Theme.kt`: add `motionScheme = MotionScheme.expressive()` to the `MaterialTheme(...)` call. This is additive — swaps default/standard Compose transitions for M3's spring-physics-based expressive motion scheme across every themed composable, with no other code changes required.

## Components

Concrete swaps, scoped to what exists in the codebase today (verified via grep, no speculative additions):

- **Loading indicators** — `CircularProgressIndicator` usages in `SplashScreen.kt`, `AudioButton.kt`, `LoadingIndicator.kt`, `PoetBottomSheetContent.kt`, `DownloadablePoetItem.kt` (5 files) switch to Material 3's new `LoadingIndicator`/`ContainedLoadingIndicator` expressive component (morphing-shape indicator, not a spinner).
- **Navbar selected-state indicator** — `Navbar.kt`'s `NavbarItem` currently marks the selected tab with a 12dp bottom underline (`bottomBorder`). Replace with an Expressive-style pill/container highlight behind the icon+label (matching the M3 `NavigationBarItem` indicator pattern), while keeping the rest of `Navbar.kt`'s custom floating-bar structure untouched — that structure (a floating, rounded, elevated bar) already matches the Expressive visual language, so it is not being replaced with the stock `NavigationBar` composable.
- **Buttons/Cards/Chips** — the 10/7/2 files using `Button`/`Card`/chip components get their shape references updated per the Shape section above; no structural/behavioral change.
- Not doing: FAB menu, split button, button groups, segmented buttons, `TabRow` — none of these composables are used anywhere in the current codebase, so there is no existing call site to "upgrade." Introducing them would be adding new UI surface, not upgrading existing UI, which is out of scope.

## Migration order

1. Bump `composeBom` + add `material-kolor` dependency. Confirm project still builds (`./gradlew assembleDebug`) before touching theme code.
2. Rewrite `Color.kt` + `Theme.kt` for generated color scheme + `motionScheme`.
3. Rewrite `Radius.kt` deletion + the 21 shape-consumer files.
4. Swap the 5 `CircularProgressIndicator` call sites.
5. Restyle `Navbar.kt`'s selected-state indicator.

Each stage should build cleanly before moving to the next.

## Testing / verification

No existing unit or UI tests cover the theme system. Verification is:
- `./gradlew assembleDebug` and `./gradlew lint` after each migration stage (compile/lint correctness).
- Manual visual check in both light and dark mode, in RTL (the app is Persian-language, RTL-first): home screen (nav bar + top bar), a poem screen (top bar with poet header reveal), settings screen, a card list screen (e.g. downloadable poets), and a loading state (e.g. `DownloadablePoetItem` while downloading). This is UI work — compiling and linting cleanly does not confirm the redesign looks correct; the app should be run and visually checked before calling this done.
