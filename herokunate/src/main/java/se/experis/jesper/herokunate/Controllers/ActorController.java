package se.experis.jesper.herokunate.Controllers;

import java.util.*;
import se.experis.jesper.herokunate.Models.Actor;
import se.experis.jesper.herokunate.Models.CommonResponse;
import se.experis.jesper.herokunate.Repositories.ActorRepository;
import se.experis.jesper.herokunate.Utils.Command;
import se.experis.jesper.herokunate.Utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ActorController {

    @Autowired
    private ActorRepository repository;

    @PostMapping("/actor")
    public ResponseEntity<CommonResponse> createActor(HttpServletRequest request, HttpServletResponse response, @RequestBody Actor actor){
        Command command = new Command(request);

        actor = repository.save(actor);

        CommonResponse comres = new CommonResponse();
        comres.data = actor;
        comres.message = "New actor with id: ";

        HttpStatus resp = HttpStatus.CREATED;

        response.addHeader("Location", "/actor/all");

        command.setResult(resp);
        Logger.getInstance().logCommand(command);
        return new ResponseEntity<>(comres, resp);
    }

    @GetMapping("/actor/all")
    public ResponseEntity<CommonResponse> getAllActors(HttpServletRequest request){
        Command command = new Command(request);

        CommonResponse commonres = new CommonResponse();
        commonres.data = repository.findAll();
        commonres.message = "All actors";

        HttpStatus resp = HttpStatus.OK;

        command.setResult(resp);
        Logger.getInstance().logCommand(command);
        return new ResponseEntity<>(commonres, resp);
    }

    @PatchMapping("/actor/{id}")
    public ResponseEntity<CommonResponse> updateActor(HttpServletRequest request, @RequestBody Actor newActor, @PathVariable Integer id) {
        Command command = new Command(request);

        CommonResponse comres = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            Optional<Actor> actorRepo = repository.findById(id);
            Actor actor = actorRepo.get();

            if(newActor.firstname != null) {
                actor.firstname = newActor.firstname;
            }
            if(newActor.lastname != null) {
                actor.lastname = newActor.lastname;
            }
            if(newActor.dateOfBirth != null) {
                actor.dateOfBirth = newActor.dateOfBirth;
            }
            if (newActor.URL != null) {
                actor.URL = newActor.URL;
            }

            repository.save(actor);

            comres.data = actor;
            comres.message = "Updated actor with id: " + actor.id;
            resp = HttpStatus.OK;
        } else {
            comres.message = "Actor not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }

        command.setResult(resp);
        Logger.getInstance().logCommand(command);
        return new ResponseEntity<>(comres, resp);
    }

    @DeleteMapping("/actor/{id}")
    public ResponseEntity<CommonResponse> deleteActor(HttpServletRequest request, @PathVariable Integer id) {
        Command command = new Command(request);

        CommonResponse comres = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            repository.deleteById(id);
            comres.message = "Deleted actor with id: " + id;
            resp = HttpStatus.OK;
        } else {
            comres.message = "Actor not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }

        command.setResult(resp);
        Logger.getInstance().logCommand(command);
        return new ResponseEntity<>(comres, resp);
    }
}
