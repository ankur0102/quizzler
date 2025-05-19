package org.project.springbootstarter.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.springbootstarter.exception.Error;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Answer;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.pojo.AnswerRequestDto;
import org.project.springbootstarter.repository.AnswerRepository;
import org.project.springbootstarter.repository.PlayerRepository;
import org.project.springbootstarter.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PlayerRepository playerRepository;

    private final static Logger logger = LogManager.getLogger(AnswerServiceImpl.class);

    public Answer saveAnswer(Answer answer) {
        // validate the time for the question started
        return answerRepository.save(answer);
    }

    public ResponseEntity<String> processAnswer(AnswerRequestDto answerRequestDto) {
        Long currentTimeInMillis = System.currentTimeMillis();
        String redisKey = getRedisKey(answerRequestDto.getSessionId(), answerRequestDto.getQuestionId());
        logger.info("MyWebSocketHandler: Getting redis key: {}", redisKey);
        Long endTime = (Long) redisService.get(redisKey);
        if (currentTimeInMillis > endTime + Constants.RELAXATION_TIME_IN_MILLIS) {
            throw new ServiceException(Error.LATE_SUBMISSION_ERROR);
        }

        if (answerRepository.existsByPlayerIdAndQuestionId(answerRequestDto.getPlayerId(), answerRequestDto.getQuestionId())) {
            throw new ServiceException(Error.SUBMISSION_ALREADY_DONE);
        }

        Optional<Player> player = playerService.getPlayerById(answerRequestDto.getPlayerId());
        if (player.isEmpty()) throw new ServiceException(Error.PLAYER_NOT_FOUND);

        Optional<Question> question = questionService.getQuestionById(answerRequestDto.getQuestionId());
        if (question.isEmpty()) throw new ServiceException(Error.QUESTION_NOT_FOUND);

        answerRepository.save(Answer.builder()
                .player(player.get()).question(question.get()).submittedAt(answerRequestDto.getSubmittedAt())
                .selectedOption(answerRequestDto.getAnswer()).correct(answerRequestDto.getAnswer()
                        .equals(question.get().getCorrectOption())).build());

        return ResponseEntity.ok(Constants.SUCCESS);
    }

    public List<Answer> findByPlayerIn(List<Player> players) {
        return answerRepository.findByPlayerIn(players);
    }

    public String getRedisKey(String sessionId, Long questionId) {
        return String.format("%s#%d", sessionId, questionId);
    }
}
