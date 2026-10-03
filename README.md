# PlayerQuery

## Introduction
PlayerQuery is a plugin with a fairly simple concept. All the time players are cheating *live* on Minecraft Servers, but attempt to make it as hard as possible to find their account, for example by obfuscating their username client-side. This plugin allows server moderators to track down those players by visual clues they are showing, such as hotbar contents or the biome they're in.

## Permissions
As of now only the permission `playerquery.use` exists, which grants full access to the plugin.

## Commands
| Command                                  | Description                                                                   |
| ---------------------------------------- | ----------------------------------------------------------------------------- |
| `/pq session new`                        | Creates a new session.                                                        |
| `/pq session delete`                     | Deletes the current session.                                                  |
| `/pq session list`                       | Lists all active sessions.                                                    |
| `/pq session info`                       | Shows information about the currently selected session.                       |
| `/pq session select <id>`                | Selects a session by its ID.                                                  |
| `/pq session candidates`                 | Shows which players match the current session's queries.                      |
| `/pq query undo`                         | Undoes the last query.                                                        |
| `/pq query biome <biome>`                | Queries players by biome.                                                     |
| `/pq query hotbar <item> [slot] [count]` | Queries players by hotbar contents. Optionally filter by slot and item count. |
| `/pq query level <level>`                | Queries players by xp level.                                                     |
| `/pq query dimension_type <dimension_type>`                | Queries players by dimension type (overworld, nether, end).                                                     |
