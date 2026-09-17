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

- `gradle/libs.versions.toml`: bump `composeBom` from `2024.09.03` to `2026.08.00` (brings stable `androidx.compose.material3:material3` 1.4.0).
- `compileSdk`/`targetSdk` bumped from 36 to 37 (compose-bom 2026.08.00's artifacts require compiling against API 37). `compileOptions` source/target compatibility bumped from Java 8 to Java 11 (required for bytecode compiled by the newer Compose libraries).
- Add new dependency `com.materialkolor:material-color-utilities:5.0.0` for seed-color → tonal-scheme generation (`Hct`, `SchemeTonalSpot`, `MaterialDynamicColors`). This is the **pure algorithm** artifact — no Compose dependency at all.
  - Originally planned to use `com.materialkolor:material-kolor` (the Compose-facing wrapper with `rememberDynamicColorScheme`), but that artifact is a Compose Multiplatform library: even its Android target depends on `org.jetbrains.compose.material3`, which forced `androidx.compose.material3` to `1.5.0-alpha17` transitively — ABI-incompatible with the `compose-foundation` version actually resolved, causing an `AbstractMethodError` crash at runtime (`CustomStyle.applyStyle` missing on `TextFieldDefaults`). Switching to `material-color-utilities` avoids the JetBrains Compose dependency chain entirely; `Theme.kt`/`Color.kt` build the `androidx.compose.material3.ColorScheme` from its output directly (see Color section).
- No AGP/Kotlin version change needed. The Compose compiler is wired via the `org.jetbrains.kotlin.plugin.compose` Gradle plugin (tied to the Kotlin version), decoupled from the Compose BOM.

## Color

`ui/theme/Color.kt`:
- Delete all `*Light`/`*Dark` hex constants that fed the manual schemes (`primaryLight`, `secondaryContainerDark`, etc. — the ~50 constants currently in the file).
- Keep a single seed constant: `val BrandSeed = Color(0xFF5B6642)` (the app's existing brand primary).
- Add `fun generateColorScheme(seedColor: Color, isDark: Boolean): ColorScheme`, built from `material-color-utilities`'s `SchemeTonalSpot(Hct.fromInt(seedColor.toArgb()), isDark, contrastLevel = 0.0)` plus `MaterialDynamicColors()`'s per-role accessors (`.primary()`, `.onPrimary()`, etc., each a `DynamicColor` resolved via `.getArgb(scheme)`), mapped into every `androidx.compose.material3.ColorScheme` constructor parameter. `SchemeTonalSpot` is the standard M3 algorithm — it keeps the seed hue dominant as `primary`. (Explicitly not `PaletteStyle.Expressive` from the higher-level `material-kolor` wrapper, which deliberately shifts hue away from the seed for a discordant/playful look — not used anyway per the dependency pivot above.)
- Add `internal fun brandScheme(isDark: Boolean): DynamicScheme`, the same `SchemeTonalSpot` construction seeded from `BrandSeed`, reused by `Theme.kt`'s custom extension color roles.
- Keep one theme-independent constant, `val neutralN95Light: Color`, computed from `brandScheme(isDark = false).neutralPalette.tone(95)` — `RandomPoemBox.kt` references this raw constant directly (not through `MaterialTheme.colorScheme`) to stay a fixed near-white regardless of app theme.

`ui/theme/Theme.kt`:
- Delete the hand-built `lightScheme`/`darkScheme` (`lightColorScheme(...)`/`darkColorScheme(...)` with ~26 named parameters each).
- Replace with `generateColorScheme(seedColor = BrandSeed, isDark = darkTheme)`, called once per recomposition based on the current `darkTheme` flag.
- The custom extension color roles (`secondaryS30/S40/S50`, `neutralN70/N95/N100`, `primary20/90`) get recomputed from `brandScheme(isAppInDarkMode()).<paletteName>.tone(<n>)` instead of referencing hardcoded hex constants (e.g. `secondaryS30` → `secondaryPalette.tone(30)`, `primary90` → `primaryPalette.tone(90)`, `neutralN70` → `neutralPalette.tone(70)`) — this matches what the original names already encoded (palette + tone stop), now sourced from the real generated palette instead of hand-picked hex.
- `dynamicColor` parameter on `JaamebaadeclientTheme` stays `false` by default — no Material You wallpaper theming.

## Shape

`ui/theme/Radius.kt`:
- Delete the file's custom scale (`radius4/6/10/12/15/17/20/30`) and the derived shapes (`ButtonShape`, `CardShape`, `ChipShape`, `SheetTopShape`, `PillShape`), along with its header comment ("Corner-radius scale from the Jaame Sokhan design system... keep exact, do not round") — this comment is superseded by the user's explicit choice to move to stock M3 Expressive shapes.
- `Theme.kt`'s `MaterialTheme(...)` call gets an explicit `shapes = Shapes(...)` using the 5 shape tiers that are actually public in stable material3 1.4.0: `extraSmall` (4dp), `small` (8dp), `medium` (12dp), `large` (16dp), `extraLarge` (28dp). (The additional Expressive tiers — `largeIncreased`, `extraLargeIncreased`, `extraExtraLarge` — exist in the 1.4.0 `Shapes` constructor but their backing properties are `internal`, i.e. not actually usable outside the material3 module in this stable release; not used.)
- All current consumers of the deleted shapes (21 files, found via `grep -rl "Radius\.\|ButtonShape\|CardShape\|ChipShape\|SheetTopShape\|PillShape"`) get updated to reference the matching `MaterialTheme.shapes.*` token instead (e.g. `CardShape` uses → `MaterialTheme.shapes.large`, `ButtonShape` uses → `MaterialTheme.shapes.medium`, `PillShape` uses → `CircleShape` or `MaterialTheme.shapes.extraExtraLarge` depending on call site — exact mapping decided per call site during implementation, matching visual intent).

## Motion (dropped)

Verified against the actual `material3-android-1.4.0-sources.jar`: `MotionScheme`, its `standard()`/`expressive()` factories, and `MaterialExpressiveTheme` are all declared `internal` in stable material3 1.4.0 — they were pulled from the stable release and only exist in `material3:1.5.0-alpha17` (pre-release, requires `@OptIn`, and is the same alpha version that caused the dependency-conflict crash during implementation — see Implementation notes below). Decision (confirmed with user): stay on stable 1.4.0, do not adopt the alpha. No motion changes are made; the app keeps Compose's default transitions.

## Components (scaled down)

Verified against the same sources jar: there is no `LoadingIndicator`/`ContainedLoadingIndicator` composable shipped in stable material3 1.4.0 (only internal design tokens exist) — like motion, the new expressive components live only in the 1.5.0-alpha line. Decision (confirmed with user): stay on stable 1.4.0.

- **Loading indicators — dropped.** `CircularProgressIndicator` call sites (`SplashScreen.kt`, `AudioButton.kt`, `LoadingIndicator.kt`, `PoetBottomSheetContent.kt`, `DownloadablePoetItem.kt`) are left unchanged.
- **Navbar selected-state indicator — kept.** This one doesn't depend on any material3-internal API; it's the app's own `Navbar.kt` composable. `NavbarItem`'s selected-tab marker (currently a 12dp bottom underline via `bottomBorder`) is replaced with a pill/container highlight behind the icon+label, matching the M3 `NavigationBarItem` indicator pattern, while keeping `Navbar.kt`'s custom floating-bar structure untouched.
- **Buttons/Cards/Chips** — the 10/7/2 files using `Button`/`Card`/chip components get their shape references updated per the Shape section above; no structural/behavioral change.
- Not doing: FAB menu, split button, button groups, segmented buttons, `TabRow` — none of these composables are used anywhere in the current codebase, and their expressive variants aren't available in stable 1.4.0 regardless.

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
