package ch.ethz.globis.isk.test.validation;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintViolation;

import static ch.ethz.globis.isk.test.validation.ValidationTestUtils.*;
import static org.junit.Assert.*;

public class PublicationConstraintTest {

    private static String CONFERENCE_ID = "conf-acm";
    private static String CONFERENCE_NAME = "ACM";
    private static String CONFERENCE_NEW_NAME = "ACM SIGMOD";
    private static Integer CONFERENCE_EDITION_1_YEAR = 2005;
    private static Integer CONFERENCE_EDITION_2_YEAR = 2006;
    private static String CONFERENCE_EDITION_1_ID = "acm-" + CONFERENCE_EDITION_1_YEAR;
    private static String CONFERENCE_EDITION_2_ID = "acm-" + CONFERENCE_EDITION_2_YEAR;
    private static String PROCEEDINGS_1_TITLE = generateProceedingsName(CONFERENCE_NAME, CONFERENCE_EDITION_1_YEAR);
    private static String PROCEEDINGS_1_ID = generatePublicationId(PROCEEDINGS_1_TITLE);
    private static String PROCEEDINGS_2_TITLE = generateProceedingsName(CONFERENCE_NAME, CONFERENCE_EDITION_2_YEAR);
    private static String PROCEEDINGS_2_ID = generatePublicationId(PROCEEDINGS_2_TITLE);
    private static Integer VALID_YEAR = 2013;

    private ConferenceEditionService conferenceEditionService;
    private ConferenceService conferenceService;
    private PublicationService publicationService;
    private ArticleService articleService;
    private InProceedingsService inProceedingsService;
    private ProceedingsService proceedingsService;
    private InCollectionService inCollectionService;
    private BookService bookService;
    private PersonService personService;
    private TransactionManager tm;

    private static final String PROCEEDINGS_NAME_FORMAT = "Proceedings of %s, %s";


    @Before
    public void setUp() {
        publicationService.setUseCache(false);
        removeTestEntities();
    }

    @After
    public void cleanUp() {
        removeTestEntities();
    }

    @Test
    public void keyIsUnique() {
        /*
        * Inserting two publications with the same key should not be possible.
        * */
        //insert a valid publication
        tm.beginTransaction();

        Article first = articleService.createEntity();
        first.setId(KEY);
        first.setTitle(TITLE);
        first.setAuthors(defaultSingleAuthor(personService));
        first.setYear(VALID_YEAR);
        assertTrue("Validation failed on ", articleService.check(first).size() == 0);

        articleService.insert(first);
        tm.commitTransaction();

        tm.beginTransaction();
        assertTrue("Insert failed ", articleService.findOne(KEY) != null);
        tm.commitTransaction();

        //try to insert a new publication with the same key, this should fail
        try {
            tm.beginTransaction();
            Book second = bookService.createEntity();
            second.setId(KEY);
            second.setTitle(TITLE);
            second.setAuthors(defaultSingleAuthor(personService));
            second.setYear(VALID_YEAR);
            assertConstraints(articleService.check(first));
            assertTrue("Validation failed on ", articleService.check(first).size() == 0);
            bookService.insert(second);
            tm.commitTransaction();
            fail("Inserted publication with duplicate key ");
        } catch (Exception ex) {
            //should be OK, we caught the exception
            tm.rollbackTransaction();
        }
    }

    @Test
    public void titleIsNotNull() {
        /*
        * Inserting a publication with null title should fail
        * */
        try {
            tm.beginTransaction();
            Article article = articleService.createEntity();
            article.setId(KEY);
            article.setAuthors(defaultSingleAuthor(personService));
            article.setTitle(null);
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Invalid object inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            Book book = bookService.createEntity();
            book.setId(KEY);
            book.setAuthors(defaultSingleAuthor(personService));
            book.setTitle(null);
            assertFalse("Valid object marked as invalid ", bookService.check(book).size() == 0);
            bookService.insert(book);
            tm.commitTransaction();
            fail("Invalid object inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            Proceedings proceedings = proceedingsService.createEntity();
            proceedings.setId(KEY);
            proceedings.setAuthors(defaultSingleAuthor(personService));
            proceedings.setTitle(null);
            assertFalse("Valid object marked as invalid ", proceedingsService.check(proceedings).size() == 0);
            proceedingsService.insert(proceedings);
            tm.commitTransaction();
            fail("Invalid object inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        //insert a valid publication
        tm.beginTransaction();

        Article first = articleService.createEntity();
        first.setId(KEY);
        first.setTitle(TITLE);
        first.setAuthors(defaultSingleAuthor(personService));
        first.setYear(VALID_YEAR);
        assertTrue("Validation failed on ", articleService.check(first).size() == 0);

        articleService.insert(first);
        tm.commitTransaction();

        tm.beginTransaction();
        assertTrue("Insert failed ", articleService.findOne(KEY) != null);
        tm.commitTransaction();

        try {
            tm.beginTransaction();
            first.setTitle(null);
            assertTrue("Validation failed on ", articleService.check(first).size() != 0);

            articleService.update(first);
            tm.commitTransaction();
            fail("Invalid object updated " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
    }

    @Test
    public void pagesHasProperPattern() {

        /*
        * Inserting a publication with an invalid pattern should fail
        * */
        tm.beginTransaction();

        Article article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        article.setPages("12-34");
        article.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", articleService.check(article).size() == 0);
        articleService.insert(article);
        tm.commitTransaction();

        tm.beginTransaction();
        assertTrue("Publication with page pattern " + article.getPages() + " should have been inserted in "
               , articleService.findOne(KEY) != null);
        tm.commitTransaction();

        removeTestEntities();

        tm.beginTransaction();
        article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        article.setPages("1234");
        article.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", articleService.check(article).size() == 0);
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Publication with page pattern " + article.getPages() + " should have been inserted in "
               , articleService.findOne(KEY) != null);
        tm.commitTransaction();
        removeTestEntities();

        tm.beginTransaction();
        article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setPages(null);
        article.setYear(VALID_YEAR);
        article.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", articleService.check(article).size() == 0);
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Publication with page pattern " + article.getPages() + " should have been inserted in "
               , articleService.findOne(KEY) != null);
        tm.commitTransaction();
        removeTestEntities();

        //insertion failure cases
        try {
            tm.beginTransaction();
            article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setYear(VALID_YEAR);
            article.setPages("1-");
            article.setAuthors(defaultSingleAuthor(personService));
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been inserted in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setYear(VALID_YEAR);
            article.setPages("a-");
            article.setAuthors(defaultSingleAuthor(personService));
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been inserted in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setYear(VALID_YEAR);
            article.setPages("1-a");
            article.setAuthors(defaultSingleAuthor(personService));
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been inserted in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setYear(VALID_YEAR);
            article.setPages("a-12");
            article.setAuthors(defaultSingleAuthor(personService));
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been inserted in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        tm.beginTransaction();
        //update failure cases
        article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        article.setPages("12-34");
        article.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", articleService.check(article).size() == 0);
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Publication with page pattern " + article.getPages() + " should have been inserted in "
               , articleService.findOne(KEY) != null);
        tm.commitTransaction();

        try {
            tm.beginTransaction();

            article.setPages("1-");
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been updated in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();

            article.setPages("a-");
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been updated in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();

            article.setPages("1-a");
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been updated in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();

            article.setPages("a-12");
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Publication with page pattern " + article.getPages() + " should have NOT been updated in "
                    );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
    }

    @Test
    public void parentValid() {
        /*
            InProceedings with no parent.
         */
        try {
            tm.beginTransaction();
            InProceedings inProceedings = inProceedingsService.createEntity();
            inProceedings.setId(KEY);
            inProceedings.setTitle(TITLE);
            inProceedings.setAuthors(defaultSingleAuthor(personService));
            inProceedings.setProceedings(null);
            inProceedings.setYear(VALID_YEAR);
            assertFalse("Validation failed on ", inProceedingsService.check(inProceedings).size() == 0);
            inProceedingsService.insert(inProceedings);
            tm.commitTransaction();
            fail("InPoceedings with null parent inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        /*
            Valid InProceedings
         */
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PARENT_KEY);
        proceedings.setTitle(PARENT_TITLE);
        proceedings.setYear(VALID_YEAR);
        proceedings.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", proceedingsService.check(proceedings).size() == 0);
        proceedingsService.insert(proceedings);
        tm.commitTransaction();

        tm.beginTransaction();
        InProceedings inProceedings = inProceedingsService.createEntity();
        inProceedings.setId(KEY);
        inProceedings.setTitle(TITLE);
        inProceedings.setYear(VALID_YEAR);
        inProceedings.setAuthors(defaultSingleAuthor(personService));
        inProceedings.setProceedings(proceedings);
        assertTrue("Validation failed on  ", inProceedingsService.check(inProceedings).size() == 0);
        inProceedingsService.insert(inProceedings);
        tm.commitTransaction();

        tm.beginTransaction();
        assertTrue("Insert failed ", inProceedingsService.findOne(KEY) != null);
        tm.commitTransaction();

        //try to update the parent to be null and update, this should fail
        try {
            tm.beginTransaction();
            inProceedings.setProceedings(null);
            assertFalse("Validation failed on ", inProceedingsService.check(inProceedings).size() == 0);
            inProceedingsService.update(inProceedings);
            tm.commitTransaction();
            fail("Updated publication with null parent on" );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        removeTestEntities();


        /*
            InCollection with no parent.
         */
        try {
            tm.beginTransaction();
            InCollection inCollection = inCollectionService.createEntity();
            inCollection.setId(KEY);
            inCollection.setTitle(TITLE);
            inCollection.setYear(VALID_YEAR);
            inCollection.setAuthors(defaultSingleAuthor(personService));
            inCollection.setParentPublication(null);
            assertFalse("Validation failed on  ", inCollectionService.check(inCollection).size() == 0);
            inCollectionService.insert(inCollection);
            tm.commitTransaction();
            fail("InCollection with null parent inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        /*
            Valid InCollection
         */
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(PARENT_KEY);
        book.setTitle(PARENT_TITLE);
        book.setYear(VALID_YEAR);
        book.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", bookService.check(book).size() == 0);
        bookService.insert(book);
        tm.commitTransaction();

        tm.beginTransaction();
        InCollection inCollection = inCollectionService.createEntity();
        inCollection.setId(KEY);
        inCollection.setTitle(TITLE);
        inCollection.setYear(VALID_YEAR);
        inCollection.setAuthors(defaultSingleAuthor(personService));
        inCollection.setParentPublication(book);
        assertTrue("Validation failed on ", inCollectionService.check(inCollection).size() == 0);
        inCollectionService.insert(inCollection);
        tm.commitTransaction();

        tm.beginTransaction();
        assertTrue("Insert failed ", inCollectionService.findOne(KEY) != null);
        tm.commitTransaction();

        //try to update the parent to be null and update, this should fail
        try {
            tm.beginTransaction();
            inCollection.setParentPublication(null);
            assertFalse("Validation failed on ", inCollectionService.check(inCollection).size() == 0);
            inCollectionService.update(inCollection);
            tm.commitTransaction();
            fail("Updated publication with null parent on" );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
        removeTestEntities();
    }

    @Test
    public void atleastOneAuthor() {
        /* Test the authors field */
        try {
            tm.beginTransaction();
            Article article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setAuthors(null);
            article.setYear(VALID_YEAR);
            
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Article with null authors inserted " );
        } catch (Exception ex) {
            //exception thrown, all is well
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            Article article = articleService.createEntity();
            article.setId(KEY);
            article.setTitle(TITLE);
            article.setAuthors(new HashSet<Person>());
            article.setYear(VALID_YEAR);

            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.insert(article);
            tm.commitTransaction();
            fail("Article with zero authors inserted " );
        } catch (Exception ex) {
            //exception thrown, all is well
            tm.rollbackTransaction();
        }

        tm.beginTransaction();
        Article article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        article.setAuthors(defaultSingleAuthor(personService));
        assertTrue("Validation failed on ", articleService.check(article).size() == 0);
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Article with valid authors NOT inserted ",
                articleService.findOne(KEY) != null);
        tm.commitTransaction();

        try {
            tm.beginTransaction();
            article.setAuthors(null);
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Invalid object updated on " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            article.setAuthors(new HashSet<Person>());
            assertFalse("Valid object marked as invalid ", articleService.check(article).size() == 0);
            articleService.update(article);
            tm.commitTransaction();
            fail("Invalid object updated on " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
        removeTestEntities();
    }

    @Test
    public void yearTest() {
        //check valid inserts
        tm.beginTransaction();
        Proceedings publication = proceedingsService.createEntity();
        publication.setId(KEY);
        publication.setTitle(TITLE);
        publication.setAuthors(defaultSingleAuthor(personService));
        publication.setYear(YEAR_CURRENT);
        assertConstraints(proceedingsService.check(publication));
        proceedingsService.insert(publication);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Object not inserted ", proceedingsService.findOne(KEY) != null);
        tm.commitTransaction();

        removeTestEntities();

        tm.beginTransaction();
        publication = proceedingsService.createEntity();
        publication.setId(KEY);
        publication.setTitle(TITLE);
        publication.setAuthors(defaultSingleAuthor(personService));
        publication.setYear(YEAR_VALID_FIRST);
        assertTrue("Validation failed on ", proceedingsService.check(publication).size() == 0);
        proceedingsService.insert(publication);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Object not inserted ", proceedingsService.findOne(KEY) != null);
        tm.commitTransaction();

        removeTestEntities();

        //check insert with an invalid year
        try {
            tm.beginTransaction();
            publication = proceedingsService.createEntity();
            publication.setId(KEY);
            publication.setTitle(TITLE);
            publication.setAuthors(defaultSingleAuthor(personService));
            publication.setYear(YEAR_INVALID_BEFORE);
            assertFalse("Validation failed on ", proceedingsService.check(publication).size() == 0);
            proceedingsService.insert(publication);
            tm.commitTransaction();
            fail("Invalid object updated. " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        try {
            tm.beginTransaction();
            publication = proceedingsService.createEntity();
            publication.setId(KEY);
            publication.setTitle(TITLE);
            publication.setAuthors(defaultSingleAuthor(personService));
            publication.setYear(YEAR_INVALID_AFTER);
            assertFalse("Validation failed on ", proceedingsService.check(publication).size() == 0);
            proceedingsService.insert(publication);
            tm.commitTransaction();
            fail("Invalid publication inserted " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }

        //check update with an invalid year
        tm.beginTransaction();
        publication = proceedingsService.createEntity();
        publication.setId(KEY);
        publication.setTitle(TITLE);
        publication.setAuthors(defaultSingleAuthor(personService));
        publication.setYear(YEAR_CURRENT);
        assertTrue("Validation failed on ", proceedingsService.check(publication).size() == 0);
        proceedingsService.insert(publication);
        tm.commitTransaction();
        tm.beginTransaction();
        assertTrue("Object not inserted ", proceedingsService.findOne(KEY) != null);
        tm.commitTransaction();

        try {
            tm.beginTransaction();
            publication.setYear(YEAR_INVALID_BEFORE);
            assertFalse("Validation failed on ", proceedingsService.check(publication).size() == 0);
            proceedingsService.update(publication);
            tm.commitTransaction();
            fail("Invalid object updated on " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
        removeTestEntities();

        try {
            tm.beginTransaction();
            publication.setYear(YEAR_INVALID_BEFORE);
            assertFalse("Validation failed on ", proceedingsService.check(publication).size() == 0);
            proceedingsService.update(publication);
            tm.commitTransaction();
            fail("Invalid object updated on " );
        } catch (Exception ex) {
            tm.rollbackTransaction();
        }
        removeTestEntities();
    }

    @Test
    public void conferenceNamePropagation() {
        //insert a valid publication and a parent publication
        tm.beginTransaction();
        
        Conference conference = conferenceService.createEntity();
        conference.setName(CONFERENCE_NAME);
        conference.setId(CONFERENCE_ID);
        
        ConferenceEdition edition1 = conferenceEditionService.createEntity();
        edition1.setId(CONFERENCE_EDITION_1_ID);
        edition1.setConference(conference);
        edition1.setYear(CONFERENCE_EDITION_1_YEAR);
        ConferenceEdition edition2 = conferenceEditionService.createEntity();
        edition2.setId(CONFERENCE_EDITION_2_ID);
        edition2.setConference(conference);
        edition2.setYear(CONFERENCE_EDITION_2_YEAR);
        conference.getEditions().add(edition1);
        conference.getEditions().add(edition2);
        
        Proceedings proceedings1 = proceedingsService.createEntity();
        proceedings1.setId(PROCEEDINGS_1_ID);
        proceedings1.setTitle(PROCEEDINGS_1_TITLE);
        proceedings1.setAuthors(defaultSingleAuthor(personService));
        proceedings1.setIsbn(ISBN_INITIAL);
        proceedings1.setConferenceEdition(edition1);
        proceedings1.setYear(CONFERENCE_EDITION_1_YEAR);
        edition1.setProceedings(proceedings1);
        
        Proceedings proceedings2 = proceedingsService.createEntity();
        proceedings2.setId(PROCEEDINGS_2_ID);
        proceedings2.setTitle(PROCEEDINGS_2_TITLE);
        proceedings2.setAuthors(defaultSingleAuthor(personService));
        proceedings2.setIsbn(ISBN_INITIAL);
        proceedings2.setYear(CONFERENCE_EDITION_2_YEAR);
        proceedings2.setConferenceEdition(edition2);
        edition2.setProceedings(proceedings2);
        
        assertTrue("Validation failed on ", proceedingsService.check(proceedings1).size() == 0);
        assertTrue("Validation failed on ", proceedingsService.check(proceedings2).size() == 0);
        proceedingsService.insert(proceedings1);
        proceedingsService.insert(proceedings2);
        conferenceService.insert(conference);
        conferenceEditionService.insert(edition1);
        conferenceEditionService.insert(edition2);

        tm.commitTransaction();

        tm.beginTransaction();
        conference = conferenceService.findOne(CONFERENCE_ID);
        assertNotNull("Conference should not be null", conference);
        assertEquals(CONFERENCE_ID, conference.getId());
        assertEquals(CONFERENCE_NAME, conference.getName());
        assertEquals(2, conference.getEditions().size());
        tm.commitTransaction();
        tm.beginTransaction();
        conference.setName(CONFERENCE_NEW_NAME);
        conferenceService.update(conference);
        
        tm.commitTransaction();
        
        tm.beginTransaction();
        conference = conferenceService.findOne(CONFERENCE_ID);
        assertNotNull("Conference should not be null", conference);
        assertEquals(CONFERENCE_ID, conference.getId());
        assertEquals(CONFERENCE_NEW_NAME, conference.getName());
        assertEquals(2, conference.getEditions().size());
        for (ConferenceEdition conferenceEdition : conference.getEditions()) {
            String expectedTitle = generateProceedingsName(conference.getName(), conferenceEdition.getYear());
            Proceedings proceedings = conferenceEdition.getProceedings();
            assertEquals(expectedTitle, proceedings.getTitle());
        }
        
        tm.commitTransaction();
    }

    @Test
    public void isbnUpdateChangesNote() {
        //insert a valid publication
        tm.beginTransaction();
        Proceedings publication = proceedingsService.createEntity();
        publication.setId(KEY);
        publication.setTitle(TITLE);
        publication.setYear(VALID_YEAR);
        publication.setAuthors(defaultSingleAuthor(personService));
        publication.setIsbn(ISBN_INITIAL);

        assertTrue("Validation failed on ", proceedingsService.check(publication).size() == 0);
        proceedingsService.insert(publication);
        tm.commitTransaction();

        assertTrue("Insert failed for ", proceedingsService.findOne(KEY) != null);

        //update the ISBN
        tm.beginTransaction();
        String oldISBN = publication.getIsbn();
        publication.setIsbn(ISBN_AFTER);
        assertTrue("Validation failed on ", proceedingsService.check(publication).size() == 0);
        proceedingsService.update(publication);
        tm.commitTransaction();

        tm.beginTransaction();
        //check if the update to ISBN was done properly
        publication = proceedingsService.findOne(KEY);
        assertNotNull("Note should not be null " + publication.getNote() +
                " ", publication.getNote());
        String expectedNote = "ISBN updated, old value was " + oldISBN;
        assertTrue("Note not changed on ISBN update ", publication.getNote().contains(expectedNote));
        tm.commitTransaction();
    }

    private void removeTestEntities() {
        tm.beginTransaction();
        removeTestEntitiesWithService(publicationService, KEY, PARENT_KEY, NEW_KEY, PROCEEDINGS_1_ID, PROCEEDINGS_2_ID);
        removeTestEntity(personService, DEFAULT_AUTHOR_ID);
        removeTestEntitiesWithService(conferenceEditionService, CONFERENCE_EDITION_1_ID, CONFERENCE_EDITION_2_ID);
        removeTestEntity(conferenceService, CONFERENCE_ID);
        tm.commitTransaction();
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setInProceedingsService(InProceedingsService inProceedingsService) {
        this.inProceedingsService = inProceedingsService;
    }

    public void setProceedingsService(ProceedingsService proceedingsService) {
        this.proceedingsService = proceedingsService;
    }

    public void setInCollectionService(InCollectionService inCollectionService) {
        this.inCollectionService = inCollectionService;
    }

    public void setConferenceEditionService(ConferenceEditionService conferenceEditionService) {
        this.conferenceEditionService = conferenceEditionService;
    }

    public void setConferenceService(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setTm(TransactionManager tm) {
        this.tm = tm;
    }

    private static String generateProceedingsName(String conferenceName, Integer year) {
        return String.format(PROCEEDINGS_NAME_FORMAT, conferenceName, year);
    }

    private static String generatePublicationId(String title) {
        return title.trim().toLowerCase().replaceAll(" +", "-");
    }
    
    @SuppressWarnings("rawtypes")
	private void assertConstraints(List<ConstraintViolation> violations) {
        for (ConstraintViolation cv: violations) {
        	System.err.println(cv);
        }
    	assertTrue("Validation failed on ", violations.size() == 0);
    }
}