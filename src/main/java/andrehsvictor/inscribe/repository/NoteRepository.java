package andrehsvictor.inscribe.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.inscribe.entity.Note;
import andrehsvictor.inscribe.entity.User;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.user = :user AND n.deleted = false")
    Page<Note> findAllByUser(User user, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.publicId = :publicId AND n.deleted = false")
    Optional<Note> findByPublicId(String publicId);
}
