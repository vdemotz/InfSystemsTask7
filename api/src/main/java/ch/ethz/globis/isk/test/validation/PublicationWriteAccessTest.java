package ch.ethz.globis.isk.test.validation;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static ch.ethz.globis.isk.test.validation.ValidationTestUtils.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PublicationWriteAccessTest {

    private static final Integer VALID_YEAR = 2013;

    protected PublicationService publicationService;

    protected ArticleService articleService;

    protected BookService bookService;

    protected ProceedingsService proceedingsService;

    protected MasterThesisService masterThesisService;

    protected PhdThesisService phdThesisService;

    protected InProceedingsService inProceedingsService;

    protected InCollectionService inCollectionService;

    protected PersonService personService;

    protected TransactionManager tm;

    private static final String VALIDATION_FAILED = "Wrong number of validation errors found.";

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
    public void insertWorksProperly() {
        Publication retrieved;
        /*
            Insert a valid article
         */
        tm.beginTransaction();
        Article article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setAuthors(defaultSingleAuthor(personService));
        article.setYear(VALID_YEAR);
        assertEquals(VALIDATION_FAILED, 0, articleService.check(article).size());
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = articleService.findOne(KEY);
        assertNotNull("Insert failed.", retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid book
         */
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(KEY);
        book.setTitle(TITLE);
        book.setYear(VALID_YEAR);
        book.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, bookService.check(book).size());
        bookService.insert(book);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = bookService.findOne(KEY);
        assertNotNull("Insert failed.", retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid proceedings
         */
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(KEY);
        proceedings.setTitle(TITLE);
        proceedings.setYear(VALID_YEAR);
        proceedings.setEditors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, proceedingsService.check(proceedings).size());
        proceedingsService.insert(proceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = proceedingsService.findOne(KEY);
        assertNotNull("Insert failed.", retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid master thesis
         */
        tm.beginTransaction();
        MasterThesis masterThesis = masterThesisService.createEntity();
        masterThesis.setId(KEY);
        masterThesis.setTitle(TITLE);
        masterThesis.setYear(VALID_YEAR);
        masterThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, masterThesisService.check(masterThesis).size());
        masterThesisService.insert(masterThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = masterThesisService.findOne(KEY);
        assertNotNull("Insert failed.", retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid phd thesis
         */
        tm.beginTransaction();
        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(KEY);
        phdThesis.setTitle(TITLE);
        phdThesis.setYear(VALID_YEAR);
        phdThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, phdThesisService.check(phdThesis).size());
        phdThesisService.insert(phdThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = phdThesisService.findOne(KEY);
        assertNotNull("Insert failed." + retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid inCollection
         */
        tm.beginTransaction();
        InCollection inCollection = inCollectionService.createEntity();
        inCollection.setId(KEY);
        inCollection.setTitle(TITLE);
        inCollection.setYear(VALID_YEAR);
        inCollection.setAuthors(defaultSingleAuthor(personService));
        inCollection.setParentPublication(defaultBook(bookService, personService));
        assertEquals(VALIDATION_FAILED, 0, inCollectionService.check(inCollection).size());
        inCollectionService.insert(inCollection);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inCollectionService.findOne(KEY);
        assertNotNull("Insert failed." + retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert a valid inProceedings
         */
        tm.beginTransaction();
        InProceedings inProceedings = inProceedingsService.createEntity();
        inProceedings.setId(KEY);
        inProceedings.setTitle(TITLE);
        inProceedings.setYear(VALID_YEAR);
        inProceedings.setAuthors(defaultSingleAuthor(personService));
        inProceedings.setProceedings(defaultProceedings(proceedingsService, personService));
        assertEquals(VALIDATION_FAILED, 0, inProceedingsService.check(inProceedings).size());
        inProceedingsService.insert(inProceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inProceedingsService.findOne(KEY);
        assertNotNull("Insert failed." + retrieved);
        assertEquals(KEY, retrieved.getId());
        assertEquals(TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
    }

    @Test
    public void deleteWorksProperly() {
        Publication retrieved;
        /*
            Insert and delete a valid article
         */
        tm.beginTransaction();
        Article article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        assingDefaultAuthor(article, personService);
        assertEquals(VALIDATION_FAILED, 0, articleService.check(article).size());
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = articleService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = articleService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
        /*
            Insert and delete a valid book
         */
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(KEY);
        book.setTitle(TITLE);
        book.setYear(VALID_YEAR);
        book.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, bookService.check(book).size());
        bookService.insert(book);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = bookService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = bookService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
        /*
            Insert and delete a valid proceedings
         */
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(KEY);
        proceedings.setTitle(TITLE);
        proceedings.setYear(VALID_YEAR);
        proceedings.setEditors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, proceedingsService.check(proceedings).size());
        proceedingsService.insert(proceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = proceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = proceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
        /*
            Insert and delete a valid master thesis
         */
        tm.beginTransaction();
        MasterThesis masterThesis = masterThesisService.createEntity();
        masterThesis.setId(KEY);
        masterThesis.setTitle(TITLE);
        masterThesis.setYear(VALID_YEAR);
        masterThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, masterThesisService.check(masterThesis).size());
        masterThesisService.insert(masterThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = masterThesisService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = masterThesisService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed. ", retrieved);
        /*
            Insert and delete a valid phd thesis
         */
        tm.beginTransaction();
        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(KEY);
        phdThesis.setTitle(TITLE);
        phdThesis.setYear(VALID_YEAR);
        phdThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, phdThesisService.check(phdThesis).size());
        phdThesisService.insert(phdThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = phdThesisService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = phdThesisService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
        /*
            Insert and delete a valid inCollection
         */
        tm.beginTransaction();
        InCollection inCollection = inCollectionService.createEntity();
        inCollection.setId(KEY);
        inCollection.setTitle(TITLE);
        inCollection.setYear(VALID_YEAR);
        inCollection.setAuthors(defaultSingleAuthor(personService));
        inCollection.setParentPublication(defaultBook(bookService, personService));
        assertEquals(VALIDATION_FAILED, 0, inCollectionService.check(inCollection).size());
        inCollectionService.insert(inCollection);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inCollectionService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = inCollectionService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
        /*
            Insert and delete a valid inProceedings
         */
        tm.beginTransaction();
        InProceedings inProceedings = inProceedingsService.createEntity();
        inProceedings.setId(KEY);
        inProceedings.setTitle(TITLE);
        inProceedings.setYear(VALID_YEAR);
        inProceedings.setProceedings(defaultProceedings(proceedingsService, personService));
        inProceedings.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, inProceedingsService.check(inProceedings).size());
        inProceedingsService.insert(inProceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inProceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        removeTestEntities();
        tm.beginTransaction();
        retrieved = inProceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNull("Delete failed.", retrieved);
    }

    @Test
    public void updateWorksProperly() {
        Publication retrieved;
        /*
            Insert and update a valid article
         */
        tm.beginTransaction();
        Article article = articleService.createEntity();
        article.setId(KEY);
        article.setTitle(TITLE);
        article.setYear(VALID_YEAR);
        article.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, articleService.check(article).size());
        articleService.insert(article);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = articleService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed. ", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, articleService.check(article).size());
        articleService.update((Article) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = articleService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid book
         */
        tm.beginTransaction();
        Book book = bookService.createEntity();
        book.setId(KEY);
        book.setTitle(TITLE);
        book.setYear(VALID_YEAR);
        book.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, bookService.check(book).size());
        bookService.insert(book);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = bookService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, bookService.check(book).size());
        bookService.update((Book) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = bookService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid proceedings
         */
        tm.beginTransaction();
        Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(KEY);
        proceedings.setTitle(TITLE);
        proceedings.setYear(VALID_YEAR);
        proceedings.setEditors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, proceedingsService.check(proceedings).size());
        proceedingsService.insert(proceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = proceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, proceedingsService.check(proceedings).size());
        proceedingsService.update((Proceedings) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = proceedingsService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid master thesis
         */
        tm.beginTransaction();
        MasterThesis masterThesis = masterThesisService.createEntity();
        masterThesis.setId(KEY);
        masterThesis.setTitle(TITLE);
        masterThesis.setYear(VALID_YEAR);
        masterThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, masterThesisService.check(masterThesis).size());
        masterThesisService.insert(masterThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = masterThesisService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, masterThesisService.check(masterThesis).size());
        masterThesisService.update((MasterThesis) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = masterThesisService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid phd thesis
         */
        tm.beginTransaction();
        PhdThesis phdThesis = phdThesisService.createEntity();
        phdThesis.setId(KEY);
        phdThesis.setTitle(TITLE);
        phdThesis.setYear(VALID_YEAR);
        phdThesis.setAuthors(defaultSingleAuthor(personService));
        assertEquals(VALIDATION_FAILED, 0, phdThesisService.check(phdThesis).size());
        phdThesisService.insert(phdThesis);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = phdThesisService.findOne(KEY);
        assertNotNull("Insert failed.", retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, phdThesisService.check(phdThesis).size());
        phdThesisService.update((PhdThesis) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = phdThesisService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid inCollection
         */
        tm.beginTransaction();
        InCollection inCollection = inCollectionService.createEntity();
        inCollection.setId(KEY);
        inCollection.setTitle(TITLE);
        inCollection.setYear(VALID_YEAR);
        inCollection.setAuthors(defaultSingleAuthor(personService));
        inCollection.setParentPublication(defaultBook(bookService, personService));
        assertEquals(VALIDATION_FAILED, 0, inCollectionService.check(inCollection).size());
        inCollectionService.insert(inCollection);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inCollectionService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, inCollectionService.check(inCollection).size());
        inCollectionService.update((InCollection) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inCollectionService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
        /*
            Insert and update a valid inProceedings
         */
        tm.beginTransaction();
        InProceedings inProceedings = inProceedingsService.createEntity();
        inProceedings.setId(KEY);
        inProceedings.setTitle(TITLE);
        inProceedings.setYear(VALID_YEAR);
        inProceedings.setAuthors(defaultSingleAuthor(personService));
        inProceedings.setProceedings(defaultProceedings(proceedingsService, personService));
        assertEquals(VALIDATION_FAILED, 0, inProceedingsService.check(inProceedings).size());
        inProceedingsService.insert(inProceedings);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inProceedingsService.findOne(KEY);
        tm.commitTransaction();
        assertNotNull("Insert failed.", retrieved);
        tm.beginTransaction();
        retrieved.setTitle(NEW_TITLE);
        assertEquals(VALIDATION_FAILED, 0, inProceedingsService.check(inProceedings).size());
        inProceedingsService.update((InProceedings) retrieved);
        tm.commitTransaction();
        tm.beginTransaction();
        retrieved = inProceedingsService.findOne(KEY);
        assertNotNull("Updated entity is null ", retrieved);
        assertEquals("Updated failed.", NEW_TITLE, retrieved.getTitle());
        tm.commitTransaction();
        removeTestEntities();
    }

    private void removeTestEntities() {
        removeTestEntitiesWithService(publicationService, KEY, PARENT_KEY, NEW_KEY);
        removeTestEntitiesWithService(personService, DEFAULT_AUTHOR_ID);
    }

    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setProceedingsService(ProceedingsService proceedingsService) {
        this.proceedingsService = proceedingsService;
    }

    public void setMasterThesisService(MasterThesisService masterThesisService) {
        this.masterThesisService = masterThesisService;
    }

    public void setPhdThesisService(PhdThesisService phdThesisService) {
        this.phdThesisService = phdThesisService;
    }

    public void setInProceedingsService(InProceedingsService inProceedingsService) {
        this.inProceedingsService = inProceedingsService;
    }

    public void setInCollectionService(InCollectionService inCollectionService) {
        this.inCollectionService = inCollectionService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setTm(TransactionManager tm) {
        this.tm = tm;
    }
}
