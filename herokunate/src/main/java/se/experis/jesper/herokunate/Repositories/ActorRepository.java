package se.experis.jesper.herokunate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.jesper.herokunate.Models.Actor;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Actor getByLastname(String lastname);
}
