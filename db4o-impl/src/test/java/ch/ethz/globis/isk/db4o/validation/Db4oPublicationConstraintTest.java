package ch.ethz.globis.isk.db4o.validation;

import ch.ethz.globis.isk.config.PersistenceConfig;
import ch.ethz.globis.isk.service.*;
import ch.ethz.globis.isk.test.validation.PublicationConstraintTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@ActiveProfiles({ "test" })
public class Db4oPublicationConstraintTest extends PublicationConstraintTest {

    @Autowired
    @Override
    public void setTm(TransactionManager tm) {
        super.setTm(tm);
    }

    @Override
    @Autowired
    public void setPublicationService(PublicationService publicationService) {
        super.setPublicationService(publicationService);
    }

    @Override
    @Autowired
    public void setArticleService(ArticleService articleService) {
        super.setArticleService(articleService);
    }

    @Override
    @Autowired
    public void setInProceedingsService(InProceedingsService inProceedingsService) {
        super.setInProceedingsService(inProceedingsService);
    }

    @Override
    @Autowired
    public void setProceedingsService(ProceedingsService proceedingsService) {
        super.setProceedingsService(proceedingsService);
    }

    @Override
    @Autowired
    public void setInCollectionService(InCollectionService inCollectionService) {
        super.setInCollectionService(inCollectionService);
    }

    @Override
    @Autowired
    public void setBookService(BookService bookService) {
        super.setBookService(bookService);
    }

    @Override
    @Autowired
    public void setPersonService(PersonService personService) {
        super.setPersonService(personService);
    }

    @Autowired
    @Override
    public void setConferenceEditionService(ConferenceEditionService conferenceEditionService) {
        super.setConferenceEditionService(conferenceEditionService);
    }

    @Autowired
    @Override
    public void setConferenceService(ConferenceService conferenceService) {
        super.setConferenceService(conferenceService);
    }
}
