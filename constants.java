public class constants{
    public static final int DBLOAD_ARG_COUNT = 3;
    public static final int DBQUERY_ARG_COUNT = 2;
    public static final int DBLOAD_PAGE_SIZE_ARG = 1;
    public static final int DBQUERY_PAGE_SIZE_ARG = 1;
    public static final int DATAFILE_ARG = 2;


    public static final int PERSON_NAME_SIZE = 24;
    public static final int BIRTH_DATE_SIZE = 4;
    public static final int BIRTH_PLACE_LABEL_SIZE = 8;
    public static final int DEATH_DATE_SIZE = 4;
    public static final int FIELD_LABEL_SIZE = 9;
    public static final int GENRE_LABEL_SIZE = 4;
    public static final int INSTRUMENTAL_LABEL_SIZE = 9;
    public static final int NATIONALITY_LABEL_SIZE = 4;
    public static final int THUMBNAIL_LABEL_SIZE = 4;
    public static final int WIKI_PAGEID_SIZE = 38;
    public static final int DESCRIPTION_SIZE = 4;
    public static final int TOTAL_SIZE =    PERSON_NAME_SIZE + 
                                            BIRTH_DATE_SIZE + 
                                            BIRTH_PLACE_LABEL_SIZE + 
                                            YEAR_SIZE + 
                                            FIELD_LABEL_SIZE + 
                                            GENRE_LABEL_SIZE + 
                                            INSTRUMENTAL_LABEL_SIZE + 
                                            NATIONALITY_LABEL_SIZE + 
                                            THUMBNAIL_LABEL_SIZE + 
                                            WIKI_PAGEID_SIZE + 
                                            DESCRIPTION_SIZE;
    public static final int PERSON_NAME_POS = 24;
    public static final int BIRTH_DATE_POS = 4;
    public static final int BIRTH_PLACE_LABEL_POS = 8;
    public static final int DEATH_DATE_POS = 4;
    public static final int FIELD_LABEL_POS = 9;
    public static final int GENRE_LABEL_POS = 4;
    public static final int INSTRUMENTAL_LABEL_POS = 9;
    public static final int NATIONALITY_LABEL_POS = 4;
    public static final int THUMBNAIL_LABEL_POS = 4;
    public static final int WIKI_PAGEID_POS = 38;
    public static final int DESCRIPTION_POS = 4;
    public static final int MILLISECONDS_PER_SECOND = 1000000;

    public static final int ID_OFFSET =   PERSON_NAME_SIZE;

    public static final int DATE_OFFSET =   PERSON_NAME_SIZE +
                                            BIRTH_DATE_SIZE;

    public static final int YEAR_OFFSET =  PERSON_NAME_SIZE +
                                            BIRTH_DATE_SIZE +
                                            BIRTH_PLACE_LABEL_SIZE;

    public static final int MONTH_OFFSET =  PERSON_NAME_SIZE +
                                            BIRTH_DATE_SIZE +
                                            BIRTH_PLACE_LABEL_SIZE +
                                            YEAR_SIZE;

    public static final int MDATE_OFFSET =  PERSON_NAME_SIZE +
                                            BIRTH_DATE_SIZE +
                                            BIRTH_PLACE_LABEL_SIZE +
                                            YEAR_SIZE +
                                            FIELD_LABEL_SIZE;

    public static final int DAY_OFFSET =   PERSON_NAME_SIZE +
                                            BIRTH_DATE_SIZE +
                                            BIRTH_PLACE_LABEL_SIZE +
                                            YEAR_SIZE +
                                            FIELD_LABEL_SIZE +
                                            GENRE_LABEL_SIZE;

    public static final int TIME_OFFSET =   PERSON_NAME_SIZE + 
                                            BIRTH_DATE_SIZE + 
                                            BIRTH_PLACE_LABEL_SIZE +
                                            YEAR_SIZE +
                                            FIELD_LABEL_SIZE +
                                            GENRE_LABEL_SIZE +
                                            INSTRUMENTAL_LABEL_SIZE;

    public static final int SENSORID_OFFSET =   PERSON_NAME_SIZE + 
                                                BIRTH_DATE_SIZE + 
                                                BIRTH_PLACE_LABEL_SIZE +
                                                YEAR_SIZE +
                                                FIELD_LABEL_SIZE +
                                                GENRE_LABEL_SIZE +
                                                INSTRUMENTAL_LABEL_SIZE +
                                                NATIONALITY_LABEL_SIZE;

    public static final int SENSORNAME_OFFSET = PERSON_NAME_SIZE + 
                                                BIRTH_DATE_SIZE + 
                                                BIRTH_PLACE_LABEL_SIZE + 
                                                YEAR_SIZE + 
                                                FIELD_LABEL_SIZE + 
                                                GENRE_LABEL_SIZE + 
                                                INSTRUMENTAL_LABEL_SIZE + 
                                                NATIONALITY_LABEL_SIZE + 
                                                THUMBNAIL_LABEL_SIZE; 

    public static final int COUNTS_OFFSET = PERSON_NAME_SIZE + 
                                            BIRTH_DATE_SIZE + 
                                            BIRTH_PLACE_LABEL_SIZE + 
                                            YEAR_SIZE + 
                                            FIELD_LABEL_SIZE + 
                                            GENRE_LABEL_SIZE + 
                                            INSTRUMENTAL_LABEL_SIZE + 
                                            NATIONALITY_LABEL_SIZE + 
                                            THUMBNAIL_LABEL_SIZE + 
                                            WIKI_PAGEID_SIZE;    


}