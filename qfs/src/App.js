import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import PlayerLobby from "./components/PlayerLobby";
import QuizPlayArea from "./components/QuizPlayArea";
import Leaderboard from "./components/Leaderboard";
import HostControlPanel from "./components/HostControlPanel";
import HostSelectQuiz from "./components/HostSelectQuiz";
import HostQuizPanel from "./components/HostQuizPanel";
import Home from "./components/Home";
import CreateQuiz from "./components/CreateQuiz"; // Import the new CreateQuiz component

const App = () => {
    const [sessionId, setSessionId] = useState(null);
    const [player, setPlayer] = useState(null);

    const onHostSessionCreated = (data) => {
        setSessionId(data.sessionId);
        setPlayer({ playerId: data.playerId, nicknam: data.nickname });
    };

    const onPlayerJoined = (data) => {
        console.log("Joined", data);
        console.log(data.sessionId);
        console.log(data.player);
        setSessionId(data.sessionId);
        setPlayer(data.player);
    };

    return (
        <Router>
            <Routes>
                {/* Home Route */}
                <Route path="/" element={<Home />} />

                {/* Other Routes */}
                <Route
                    path="/host"
                    element={<HostControlPanel onHostSessionCreated={(data) => onHostSessionCreated(data)} />}
                />
                <Route path="/createQuiz" element={<CreateQuiz />} /> {/* New Route */}
                <Route path="/selectQuiz" element={<HostSelectQuiz />} />
                <Route path="/quizPanel" element={<HostQuizPanel />} />
                <Route
                    path="/lobby"
                    element={<PlayerLobby onPlayerJoined={(data) => onPlayerJoined(data)} />}
                />
                <Route path="/play" element={<QuizPlayArea />} />
                <Route path="/leaderboard" element={<Leaderboard />} />
            </Routes>
        </Router>
    );
};

export default App;