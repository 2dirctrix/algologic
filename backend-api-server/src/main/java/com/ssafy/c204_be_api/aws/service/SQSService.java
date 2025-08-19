package com.ssafy.c204_be_api.aws.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.c204_be_api.aws.config.AWSProperties;
import com.ssafy.c204_be_api.aws.service.message.JudgeRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSService {

    private final SqsClient sqsClient;
    private final AWSProperties awsProperties;

    private static final String JUDGE_REQUEST_MSG_GROUP_ID = "judge-request";

    public void sendToJudgeQueue(JudgeRequestMessage message) {
        try {
            String messageToJson = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(message);
            String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                            .queueName(awsProperties.getSqsQueueName())
                            .build())
                    .queueUrl();

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageGroupId(JUDGE_REQUEST_MSG_GROUP_ID)
                    .messageDeduplicationId(message.memberId() + "-" + System.currentTimeMillis())
                    .messageBody(messageToJson)
                    .build();

            sqsClient.sendMessage(sendMsgRequest);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("메시지를 JSON으로 변환하는데 실패하였습니다.", e);
        }
    }

}
