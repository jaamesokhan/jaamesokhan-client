# Firebase Analytics events

All events are emitted through `analytics/AnalyticsLogger.kt`.

| Event | Params | When |
|---|---|---|
| `screen_view` | `screen_name` | Every navigation destination change (route without args) |
| `notification_open` | `destination` | App opened from a daily-poem notification |
| `poem_view` | `poet_id`, `poet_name`, `poem_id`, `poem_title` | Poem opened |
| `random_poem_refresh` | – | User asks for a new random poem |
| `verses_copy` | `poem_id`, `count`, `source` | Verses / selected text copied |
| `share` | `content_type`, `item_id` | Verses, notes or saved items shared |
| `dictionary_lookup` | `poem_id` | Word meaning requested |
| `search` | `search_term`, `result_count`, `poet_filter_count` | Search submitted |
| `bookmark_add` / `bookmark_remove` | `poem_id` | Bookmark toggled |
| `highlight_add` | `poem_id`, `count` | Highlight created (`count` = verses spanned) |
| `highlight_color_change` / `highlight_remove` | `poem_id` | Highlight edited / removed |
| `note_add` | `poem_id`, `length` | Note written |
| `note_delete` | `source` | Note deleted (`poem` or `my_notes`) |
| `saved_item_remove` | `type` | Bookmark/highlight removed from saved list |
| `category_create` / `category_assign` | `type`, `count` | Save categories created / assigned |
| `category_delete` | – | Save category deleted |
| `history_clear` / `history_item_delete` | – | History cleared / item removed |
| `poet_download_start` | `poet_id`, `poet_name` | Poet download started |
| `poet_download_success` / `poet_download_failed` | `poet_id`, `poet_name`, `duration_ms` | Poet download finished |
| `poet_delete` | `poet_id`, `poet_name` | Downloaded poet removed |
| `recitations_load` | `poem_id`, `count`, `result` | Recitation list fetched |
| `recitation_play` / `recitation_complete` | `poem_id`, `artist_name` | Recitation started / finished |
| `recitation_error` | `poem_id` | Recitation failed to load |
| `playback_speed_change` / `playback_repeat_toggle` | `value` | Player controls |
| `setting_change` | `setting`, `value` | Theme, font, font size, random-poem layout, daily notification |

## User properties

`downloaded_poets_count`, `app_theme`, `poem_font`, `poem_font_size`, `random_poem_layout`, `daily_poem_notification`

To use custom params in reports (e.g. top poets by `poem_view`), register them as custom dimensions/metrics in the Firebase console → Analytics → Custom definitions. User properties must be registered there too.
