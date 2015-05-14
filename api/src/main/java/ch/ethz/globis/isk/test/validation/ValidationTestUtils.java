package ch.ethz.globis.isk.test.validation;

import ch.ethz.globis.isk.domain.Book;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.BookService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.ProceedingsService;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public final class ValidationTestUtils {

    public static final String KEY = "key-test";
    public static final String NEW_KEY = "new-key-test";
    public static final String TITLE = "title-test";
    public static final String PARENT_KEY = "parent-key-test";
    public static final String PARENT_TITLE = "parent-title-test";
    public static final String NEW_PUBLICATION_NOTE = "New publication";
    public static final String ISBN_CHANGED_NOTE = "ISBN updated";

    public static final Integer YEAR_CURRENT = Calendar.getInstance().get(Calendar.YEAR);
    public static final Integer YEAR_INVALID_BEFORE = 1900;
    public static final Integer YEAR_VALID_FIRST = YEAR_INVALID_BEFORE + 1;
    public static final Integer YEAR_INVALID_AFTER = YEAR_CURRENT + 1;
    public static final String ISBN_INITIAL = "1-84356-028-3";
    public static final String ISBN_AFTER = "0-684-84328-5";

    public static final String DEFAULT_AUTHOR_ID = "1234567";
    public static final String DEFAULT_AUTHOR_NAME = "default-author-name-test";

    public static final String NEW_TITLE = "new-title-test";
    private static final Integer VALID_YEAR = 2013;

    public static Proceedings defaultProceedings(ProceedingsService proceedingsService, PersonService personService) {
        final Proceedings proceedings = proceedingsService.createEntity();
        proceedings.setId(PARENT_KEY);
        proceedings.setTitle(PARENT_TITLE);
        proceedings.setYear(VALID_YEAR);
        proceedings.setEditors(defaultSingleAuthor(personService));
        proceedingsService.insert(proceedings);
        return proceedings;
    }

    public static Book defaultBook(BookService bookService, PersonService personService) {
        final Book book = bookService.createEntity();
        book.setId(PARENT_KEY);
        book.setTitle(PARENT_TITLE);
        book.setYear(VALID_YEAR);
        book.setAuthors(defaultSingleAuthor(personService));
        bookService.insert(book);
        return book;
    }

    public static void assingDefaultAuthor(Publication publication, PersonService personService) {
        Set<Person> persons = defaultSingleAuthor(personService);
        publication.setAuthors(persons);
        for (Person person : persons) {
            person.getAuthoredPublications().add(publication);
        }
    }

    public static Set<Person> defaultSingleAuthor(PersonService personService) {
        final Person existingPerson = personService.findOne(DEFAULT_AUTHOR_ID);
        if (existingPerson != null) {
            return new HashSet<Person>() { {
                add(existingPerson);
            }};
        }

        final Person defaultPerson = personService.createEntity();
        defaultPerson.setId(DEFAULT_AUTHOR_ID);
        defaultPerson.setName(DEFAULT_AUTHOR_NAME);
        personService.insert(defaultPerson);
        return new HashSet<Person>() { {
            add(defaultPerson);
        }};
    }

    public static void removeTestEntitiesWithService(BaseService baseService, String... ids) {
        for (String id : ids) {
            removeTestEntity(baseService, id);
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeTestEntity(BaseService baseService, String id) {
        baseService.delete(baseService.findOne(id));
    }
}