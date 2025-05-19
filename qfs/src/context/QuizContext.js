import React, { createContext, useContext, useState } from 'react';

const QuizContext = createContext();

export function useQuiz() {
    return useContext(QuizContext);
}

export function QuizProvider({ children }) {
    const [session, setSession] = useState(null);
    const [player, setPlayer] = useState(null);
    const [quiz, setQuiz] = useState(null);

    return (
        <QuizContext.Provider value={{
            session, setSession,
            player, setPlayer,
            quiz, setQuiz,
        }}>
            {children}
        </QuizContext.Provider>
    );
}