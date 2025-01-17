package com.moneybricks.stock.controller;


import com.moneybricks.stock.domain.Player;
import com.moneybricks.stock.dto.GameStartRequest;
import com.moneybricks.stock.dto.GameStatusResponse;
import com.moneybricks.stock.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {
    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<GameStatusResponse> startGame(@RequestBody GameStartRequest request) {
        Player player = gameService.startNewGame(request.getInitialCash());
        return ResponseEntity.ok(GameStatusResponse.from(player));
    }
}
