package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.commands.AgregarMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregarDirectrizMentoriaUseCaseTest {

        @InjectMocks
        private AgregarDirectrizMentoriaUseCase useCase;

        @Mock
        private DomainEventRepository repository;

        @Test
        void  addDirectiveToMentory(){
            CursoId coursoId = CursoId.of("ddd1");
            MentoriaId mentoriaId = MentoriaId.of("sdf");
            Directiz directiz =new Directiz("direcctis");
            var command = new AgregarDirectrizMentoria( coursoId,  mentoriaId,  directiz);

            when(repository.getEventsBy("ddd1")).thenReturn(history());
            useCase.addRepository(repository);
            //act
            var events = UseCaseHandler.getInstance()
                    .setIdentifyExecutor(command.getCursoId().value())
                    .syncExecutor(useCase, new RequestCommand<>(command))
                    .orElseThrow()
                    .getDomainEvents();

            //assert
            var event = (DirectrizAgregadaAMentoria)events.get(0);
            Assertions.assertEquals("direcctis", event.getDirectiz().value());

        }
    private List<DomainEvent> history() {
        Nombre nombre = new Nombre("DDD");
        Descripcion descripcion = new Descripcion("Curso complementario para el training");

        var event = new CursoCreado(
                nombre,
                descripcion
        );
        var event2 = new MentoriaCreada(
                MentoriaId.of("sdf"),
                new Nombre("nameMentori"),
                new Fecha(LocalDateTime.now(), LocalDate.now())
        );
        event.setAggregateRootId("xxxxx");
        return List.of(event,event2);
    }

}