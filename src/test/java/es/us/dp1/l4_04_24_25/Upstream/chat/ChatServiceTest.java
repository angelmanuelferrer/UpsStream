package es.us.dp1.l4_04_24_25.Upstream.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.player.Player;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveMessage() {
        Board board = new Board();
        Player player = new Player();
        String message = "Hola";
        Message chatMessage = new Message();
        chatMessage.setBoard(board);
        chatMessage.setPlayer(player);
        chatMessage.setMessage(message);

        when(chatRepository.save(any(Message.class))).thenReturn(chatMessage);

        Message savedMessage = chatService.saveMessage(board, player, message);

        assertNotNull(savedMessage);
        assertEquals(board, savedMessage.getBoard());
        assertEquals(player, savedMessage.getPlayer());
        assertEquals(message, savedMessage.getMessage());
    }

    @Test
    public void testGetMessagesByBoard() {
        Board board = new Board();
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> messages = Arrays.asList(message1, message2);

        when(chatRepository.findByBoard(board)).thenReturn(messages);

        List<Message> result = chatService.getMessagesByBoard(board);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetMessagesByPlayer() {
        Player player = new Player();
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> messages = Arrays.asList(message1, message2);

        when(chatRepository.findByPlayer(player)).thenReturn(messages);

        List<Message> result = chatService.getMessagesByPlayer(player);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreateChatForBoard() {
        Board board = new Board();
        Message chatMessage = new Message();
        chatMessage.setBoard(board);

        when(chatRepository.save(any(Message.class))).thenReturn(chatMessage);

        Message createdMessage = chatService.createChatForBoard(board);

        assertNotNull(createdMessage);
        assertEquals(board, createdMessage.getBoard());
    }

    @Test
    public void testDelete() {
        Integer id = 1;

        doNothing().when(chatRepository).deleteById(id);

        chatService.delete(id);

        verify(chatRepository, times(1)).deleteById(id);
    }
}