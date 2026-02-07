package es.us.dp1.l4_04_24_25.Upstream.board;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.us.dp1.l4_04_24_25.Upstream.salmon.Salmon;
import es.us.dp1.l4_04_24_25.Upstream.user.User;

public interface BoardRepository extends CrudRepository<Board, Integer> {

    List<Board> findAll();

    @Query("SELECT b FROM Board b JOIN b.match m WHERE m.id = :id")
    Optional<Board> findBoardByMatchId(@Param("id") int id) throws DataAccessException;

    @Query("SELECT s FROM Salmon s WHERE s.board.id = :boardId")
    List<Salmon> findSalmonsByBoardId(@Param("boardId") int boardId) throws DataAccessException;

    @Query("SELECT b FROM Board b WHERE not exists (SELECT j FROM b.match.players j WHERE not :user member of j.user.friends)")
    List<Board> findBoardsByUserFriends(@Param("user") User user) throws DataAccessException;

}
