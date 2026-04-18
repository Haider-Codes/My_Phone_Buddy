package com.myphonebuddy.controllers;

import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.modal.FeedbackInfo;
import com.myphonebuddy.service.FeedbackService;
import com.myphonebuddy.utility.utils.BuilderUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feedback")
@Slf4j
public class FeedbackController {

    private final  FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    private String processFeedback(@Valid @ModelAttribute FeedbackInfo feedbackInfo,
                                   BindingResult  bindingResult, HttpSession httpSession) {

        Acknowledgement acknowledgement = null;
        if(bindingResult.hasErrors()) {
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Please correct invalid data");
            httpSession.setAttribute("acknowledgement", acknowledgement);
            return "contact";
        }

        var saved_feedback = feedbackService.saveFeedback(BuilderUtil.buildFeedback(feedbackInfo));


        if(saved_feedback != null) {
            log.info("Feedback saved successfully");
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.SUCCESS, "Message sent successfully");
        }
        else {
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Something went wrong while sending the message");
        }

        httpSession.setAttribute("acknowledgement", acknowledgement);
        return "redirect:/contact";

    }

}
