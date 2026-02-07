package es.us.dp1.l4_04_24_25.Upstream.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;

@Service
public class ChatService { 

    private ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
    
    @Transactional
    public Message saveMessage(Board board, Player player, String message) {
        Message chat = new Message();
        chat.setBoard(board);
        chat.setPlayer(player);
        chat.setMessage(message);
        return chatRepository.save(chat);
    }

    // Obtener mensajes por partida
    @Transactional(readOnly = true)
    public List<Message> getMessagesByBoard(Board board) {
        return chatRepository.findByBoard(board);
    }

    // Obtener mensajes por jugador (opcional)
    @Transactional(readOnly = true)
    public List<Message> getMessagesByPlayer(Player player) {
        return chatRepository.findByPlayer(player);
    }

    // Crear chat para partida
    @Transactional
    public Message createChatForBoard(Board board) {
        Message chat = new Message();
        chat.setBoard(board);
        return chatRepository.save(chat);
    }

    @Transactional
    public void delete(Integer id) {
        chatRepository.deleteById(id);
    }

}
