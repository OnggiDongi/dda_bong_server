package com.hana7.ddabong.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBroadcastController {
	private static int msgId = 0;

//	@MessageMapping("/broadcast")   // message는 /app/broadcast로 보냄
//	@SendTo("/topic/broadcast")
//	public ChatMessage send(ChatMessage msg) throws Exception {
//		return new ChatMessage(++msgId, msg.from(), msg.text());
//	}
//
//	@MessageMapping("/join-leave")
//	@SendTo("/topic/join-leave")
//	public ChatMessage joinLeave(ChatMessage msg) throws Exception {
//		return new ChatMessage(++msgId, "system", msg.from()
//				+ "'s " + msg.text());
//	}
}

