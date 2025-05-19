# Quizzler

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Quizzler is a real-time multiplayer quiz game that offers server-controlled timing, basic scoring, and a host-controlled quiz flow. The app allows multiple players to join a quiz session using a unique room code, ensuring they receive questions in perfect sync.

---

## **Table of Contents**
1. [Features](#features)
2. [Installation](#installation)
3. [SwaggerURL](#swaggerurl)
4. [DatabaseSchema](#DatabaseSchema)

---

## **Features**

- ✨ **User Roles:** Users can join the app as either a Host or a Player.
- ✨ **Quiz Creation:** Hosts can create quizzes and add custom questions.
- ✨ **Session Management:** Hosts can start a session/room and share a unique session code with players.
- ✨ **Player Participation:** Players can easily join a session by entering the unique session/room code.
- ✨ **Real-Time Synchronization:** Questions are delivered to all players simultaneously when the host advances to the next question.
- ✨ **Leaderboard Updates:** After each question, the leaderboard is refreshed with the latest scores.
- ✨ **Timed Responses:** Players must answer questions within a set time limit; late responses are automatically rejected by the server.
- ⚡ **Mobile-Responsive UI:** The app's interface is fully optimized for mobile devices, ensuring seamless user experience across all screen sizes.
- ✨ **Hassle-Free Setup:** Set up Quizzler on your local machine with minimal steps.
- ✅ **Backend Unit Tests:** Unit tests are implemented for the backend, ensuring robust and reliable functionality.
---
## **Installation**

Follow these steps to install and run the project locally:

1. **Pre-requisite**:
Make sure the following ports are free in your local machine:
- 3000 - frontend service
- 8080 - backend service
- 5432 - postgres
- 6379 - redis
2. **Clone the repository**:
   ```bash
   git clone <repository-url.git>
   
    ```
3. **Go to the repository root**:
   ```bash
   cd quizzler
    ```
4. **Execute the Docker command**:
   ```bash
   docker-compose up --build -d
    ```
---
## **SwaggerURL**
Once all services are up and running, visit the following URL to view the complete list of implemented APIs:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
## **DatabaseSchema**
**Table: players**

| Column Name | Data Type	   | Description                                           | 
|-------------|--------------|-------------------------------------------------------|
| id          | bigint       | PRIMARY KEY                                           |
| session_id  | VARCHAR(255) | FOREIGN KEY (session_id) REFERENCES game_sessions(id) |
| nickname    | VARCHAR(255) | NAME OF THE PLAYER                                    |
| joined_at   | bigint       | TIME WHEN PLAYER JOINED THE ROOM                      |

**Table: quizzes**

| Column Name | Data Type	    | Description                                           | 
|-------------|---------------|-------------------------------------------------------|
| id          | bigint        | PRIMARY KEY                                           |
| created_at  | VARCHAR(255)  | FOREIGN KEY (session_id) REFERENCES game_sessions(id) |
| metadata    | oid           | USED TO STORE METADATA                                |
| title       | VARCHAR(255)  | TITLE OF THE QUIZ                                     |


**Table: questions**

| Column Name    | Data Type	    | Description                                  | 
|----------------|---------------|----------------------------------------------|
| id             | bigint        | PRIMARY KEY                                  |
| correct_option | VARCHAR(255)  | USED TO STORE THE CORRECT OPTION             |
| text           | VARCHAR(255)  | USED TO STORE TEXT OF A QUESTION             |
| quiz_id        | bigint        | FOREIGN KEY (quiz_id) REFERENCES quizzes(id) |

**Table: game_sessions**

| Column Name            | Data Type	    | Description                                               | 
|------------------------|---------------|-----------------------------------------------------------|
| id                     | VARCHAR(255)  | PRIMARY KEY                                               |
| current_question_index | integer       | USED TO STORE THE CURRENT INDEX OF THE QUIZ               |
| started_at             | bigint        | USED TO STORE STARTING TIME OF A SESSION                  |
| status                 | VARCHAR(255)  | STORE THE STATUS OF THE SESSION (new, started, completed) |
| quiz_id                | bigint        | FOREIGN KEY (quiz_id) REFERENCES quizzes(id)              |

**Table: answers**

| Column Name     | Data Type	   | Description                                               | 
|-----------------|--------------|-----------------------------------------------------------|
| id              | bigint       | PRIMARY KEY                                               |
| correct         | boolean      | USED TO STORE THE CURRENT INDEX OF THE QUIZ               |
| selected_option | VARCHAR(255) | USED TO STORE STARTING TIME OF A SESSION                  |
| submitted_at    | bigint       | STORE THE STATUS OF THE SESSION (new, started, completed) |
| player_id       | bigint       | FOREIGN KEY (player_id) REFERENCES players(id)            |
| question_id     | bigint       | FOREIGN KEY (question_id) REFERENCES questions(id)        |

