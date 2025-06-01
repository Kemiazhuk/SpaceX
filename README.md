# SpaceX Dragon Rockets

A simple in-memory Java library to manage the status of SpaceX Dragon rockets and missions.

## Features
- Add rockets and missions
- Assign rockets to missions
- Change rocket and mission statuses
- Auto-update mission status
- Mission summary by rocket count

- **Rocket Statuses:**
    - `ON_GROUND` (default)
    - `IN_SPACE`
    - `IN_REPAIR`
    - `IN_BUILD`

- **Mission Statuses:**
    - `SCHEDULED` (default)
    - `PENDING` (if any rocket is in repair)
    - `IN_PROGRESS` (if at least one assigned rocket and none in repair)
    - `ENDED` (no rockets can be assigned after this)

## How AI (ChatGPT) Helped
ChatGPT helped me make some parts of the code more readable and cleaner.
