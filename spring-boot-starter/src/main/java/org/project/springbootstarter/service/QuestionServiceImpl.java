package org.project.springbootstarter.service;

import org.project.springbootstarter.exception.Error;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuestionDto;
import org.project.springbootstarter.repository.QuestionRepository;
import org.project.springbootstarter.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizService quizService;

    public ResponseEntity<String> addQuestionToQuiz(List<QuestionDto> questions, Long quizId) {
        Optional<Quiz> quiz = quizService.findById(quizId);

        if (quiz.isEmpty()) throw new ServiceException(Error.QUIZ_NOT_FOUND);

        questionRepository.saveAll(questions.stream().map(questionDto -> Question.builder().text(questionDto.getText())
                .options(questionDto.getOptions()).correctOption(questionDto.getCorrectOption()).quiz(quiz.get()).build())
                .collect(Collectors.toList()));

        return ResponseEntity.ok(Constants.SUCCESS);
    }

    public Optional<Question> getQuestionById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public List<Question> findByQuiz(Quiz quiz) {
        return questionRepository.findByQuiz(quiz);
    }
}
