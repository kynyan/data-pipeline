package project.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import project.PersistenceConfig;
import project.model.Message;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = PersistenceConfig.class)
public class MessageRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void mustReturnMessageFromDbAsItWasSaved() {
        Message message = Message.random().setId(null);
        Message saved = messageRepository.save(message);
        flushAndClear();
        Message fromDb = messageRepository.findById(saved.getId()).get();
        assertReflectionEquals(saved, fromDb);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
