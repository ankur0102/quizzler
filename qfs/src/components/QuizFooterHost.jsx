import React, { useState, useEffect } from 'react';
import {Button, Card} from "react-bootstrap";

const QuizFooterHost = ({ endTime, isLastQuestion, onClickNext, onResult }) => {
    const calculateRemainingTime = () => Math.max(0, Math.floor((endTime - Date.now()) / 1000));

    const [timeLeft, setTimeLeft] = useState(calculateRemainingTime); // Countdown timer state
    const [disableNextButton, setDisableNextButton] = useState(true);
    const onTimeUp = () => {
        setDisableNextButton(false);
    }

    const onClickNextButton = (callback) => {
        setDisableNextButton(true);
        callback();
    }

    useEffect(() => {
        setTimeLeft(calculateRemainingTime());
        if (disableNextButton === false) setDisableNextButton(true);
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
    }, [endTime]);

    return (
        <Card.Footer className="d-flex justify-content-between align-items-center">
            <div>
                <strong>Time Left: {timeLeft} seconds</strong>
            </div>
            <Button variant="primary"
                    disabled={disableNextButton}
                    onClick={()=>onClickNextButton(onClickNext)}>
                {isLastQuestion?`View Results`:`Next Question`}
            </Button>
        </Card.Footer>
    );
};

export default QuizFooterHost;