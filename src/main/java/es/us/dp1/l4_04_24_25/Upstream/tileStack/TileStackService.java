package es.us.dp1.l4_04_24_25.Upstream.tileStack;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.l4_04_24_25.Upstream.board.Board;
import es.us.dp1.l4_04_24_25.Upstream.board.BoardService;
import es.us.dp1.l4_04_24_25.Upstream.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TileStackService {

    private TileStackRepository tileStackRepository;

    private BoardService boardService;

    @Autowired
    public TileStackService(TileStackRepository tileStackRepository, BoardService boardService) {
        this.tileStackRepository = tileStackRepository;
        this.boardService = boardService;

    }

    @Transactional(readOnly = true)
    public List<TileStack> getAllTileStacks() {
        return tileStackRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TileStack> getTileStackById(Integer id) {
        return tileStackRepository.getTileStackById(id);
    }

    @Transactional
    public TileStack save(TileStack ts) {
        return tileStackRepository.save(ts);
    }

    @Transactional
    public void delete(Integer id) {
        tileStackRepository.deleteById(id);
    }

    @Transactional
    public void actualizeTile(Integer boardId) {
        List<TileStack> tileStacks = tileStackRepository.getTileStackByBoardId(boardId);
        if (tileStacks.isEmpty()) {
            throw new ResourceNotFoundException("TileStack", "boardId", boardId);
        }

        List<TileStack> unusedTileStacks = tileStacks.stream()
                .filter(ts -> !ts.getUsed())
                .collect(Collectors.toList());

        if (unusedTileStacks.isEmpty()) {
            log.info(String.format("All TileStacks for Board ID: %s have been used.", boardId));
            return; 
        }

        Random random = new Random();
        int randomIndex = random.nextInt(unusedTileStacks.size());
        TileStack selectedTileStack = unusedTileStacks.get(randomIndex);

        Board board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        board.setTileStack(selectedTileStack);

        selectedTileStack.setUsed(true);

        boardService.save(board);
        tileStackRepository.save(selectedTileStack);
    }

    @Transactional(readOnly = true)
    public TileStack getTileStackByBoardId(Integer boardId) {

        return boardService.getBoardById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId)).getTileStack();

    }

    
}
