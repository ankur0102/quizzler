import React, { useState, useEffect } from 'react';

const CountdownTimer = ({ endTime, onTimeUp }) => {
    const calculateRemainingTime = () => Math.max(0, Math.floor((endTime - Date.now()) / 1000));
    const [timeLeft, setTimeLeft] = useState(calculateRemainingTime);
    useEffect(() => {
        setTimeLeft(calculateRemainingTime());
        const timer = setInterval(() => {
            setTimeLeft((prevTime) => {
                if (prevTime <= 1) {
                    clearInterval(timer);
                    if (onTimeUp) onTimeUp();
                    return 0;
                }
                return prevTime - 1;
            });
        }, 1000);
        return () => clearInterval(timer);
    }, [endTime, onTimeUp]);

    return (
        <div>
            <strong>Time Left: {timeLeft} seconds</strong>
        </div>
    );
};

export default CountdownTimer;