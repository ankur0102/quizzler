import axios from "./api";

const backendUrl = process.env.REACT_APP_BACKEND_URL_WS || "ws://localhost:8080";
console.log("Websocket URL is ", backendUrl);

export const subscribeToSSE = (sessionId, state, callback) => {
    console.log("Initialising new socket connection");
    const socket = new WebSocket(`${backendUrl}/events/${sessionId}`);
    const { quiz, playerId, host } = state;
    console.log("Player is ", playerId, quiz);

    socket.addEventListener("open", () => {
        try {
            if (host === true) {
                const response = axios.post(`/sessions/${sessionId}/start`, {quiz, playerId}).catch(error => {
                    if (error.response) {
                        console.error('Error Response:', error.response.data);
                        console.error('Status Code:', error.response.status);
                    } else if (error.request) {
                        console.error('No Response Received:', error.request);
                    } else {
                        console.error('Error Message:', error.message);
                    }
                })
            }
            console.log('Connection established.');
        } catch (error) {
            console.error("Error starting connection:", error);
        }
    });

    socket.addEventListener("message", (event) => {
        try {
            const jsonResponse = JSON.parse(event.data);
            callback(jsonResponse);
        } catch (err) {
            console.error('Error parsing JSON:', err);
        }
    });

    socket.addEventListener('error', (err) => {
        console.error('Error encountered:', err.message);
    });

    socket.addEventListener('close', (event) => {
        console.log(`Connection closed. Code: ${event.code}, Reason: ${event.reason || 'No reason provided'}`);
    });

    return () => {
        if (socket) socket.close();
    };
};