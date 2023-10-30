package me.zhengjie.util;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class OpenAiApiExample {
    public static void main(String... args) {
        String token = "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";
        OpenAiService service = new OpenAiService(token);

        System.out.println("\nCreating completion...");
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .model("gpt-3.5-turbo")
//                .prompt("my name is xuan,and you?")
//                .echo(true)
//                .user("testing")
//                .n(1)
//                .build();


//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("一个机器人在与人类交谈，背后是星辰大海,图片宽高比是16:9")
                .build();

        System.out.println("\nImage is located at:");
        System.out.println(service.createImage(request).getData().get(0).getUrl());

//        System.out.println("Streaming chat completion...");
//        final List<ChatMessage> messages = new ArrayList<>();
//        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
//        messages.add(systemMessage);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .model("gpt-3.5-turbo")
//                .messages(messages)
//                .n(1)
//                .maxTokens(80)
//                .logitBias(new HashMap<>())
//                .build();
////        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//
//        service.streamChatCompletion(chatCompletionRequest).
//                doOnError(Throwable::printStackTrace)
//                .blockingForEach(System.out::println);

        service.shutdownExecutor();
    }
}