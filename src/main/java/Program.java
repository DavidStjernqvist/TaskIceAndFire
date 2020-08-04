import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Program {
    private static final ApiCall apiCall = new ApiCall();

    public static void main(String[] args) {

        //Gets the input from the user
        int characterId = inputCharacter();
        String characterURL = "https://anapioficeandfire.com/api/characters/" + characterId;

        //Gets the character from api
        JSONObject jsonCharacter = apiCall.getObject(characterURL);

        printCharacter(jsonCharacter);

        //User gets choice of displaying all the sworn members in the household
        displayHouseHold(jsonCharacter);

        //Gets all of the books and puts in json array
        String booksURL = "https://www.anapioficeandfire.com/api/books/";
        JSONArray jsonBooks = apiCall.getArray(booksURL);
        //Creates an ArrayList with the objects
        ArrayList<JSONObject> books = getBooks(jsonBooks);

        //Gets all the books from the publisher
        String publisher = "Bantam Books";
        ArrayList<JSONObject> publishersBooks = booksPerPublisher(books, publisher);

        printPovCharactersPerBook(publisher, publishersBooks);

    }
    private static Integer inputCharacter(){
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter id of the character: ");
        int characterId = scan.nextInt();
        return characterId;
    }
    //Prints out information about the character
    private static void printCharacter(JSONObject json){
        System.out.println("--------- Character ---------");
        System.out.println("Name: " + json.get("name"));
        System.out.println("Gender: " + json.get("gender"));
        System.out.println("Culture: " + json.get("culture"));
        System.out.println("-----------------------------\n");
    }
    private static void displayHouseHold(JSONObject jsonCharacter){
        ArrayList<String> houseURLs = createJsonStringList(jsonCharacter, "allegiances");

        Scanner scan = new Scanner(System.in);
        System.out.println("Do you want to display the sworn members of the household " + "? (yes/no)");
        String input = scan.nextLine();
        switch (input){
            case "yes":
                printHouseHold(houseURLs);
                break;
            case "no":
                break;
            default:
                return;
        }
    }
    //Prints the household and its sworn members
    private static void printHouseHold(ArrayList<String> houseUrls){
        for (int i = 0; i < houseUrls.size(); i++) {
            JSONObject jsonHouses = apiCall.getObject(houseUrls.get(i));
            System.out.println("Sworn members of " + jsonHouses.get("name"));
            ArrayList<String> swornMembersURL = createJsonStringList(jsonHouses, "swornMembers");
            printHouseMembers(swornMembersURL);
            System.out.println("------------------------------------------------");
        }
    }
    //Prints out the sworn members
    private static void printHouseMembers(ArrayList<String> swornMembersURL){
        JSONObject json = null;
        for (int i = 0; i < swornMembersURL.size(); i++) {
            json = apiCall.getObject(swornMembersURL.get(i));
            System.out.println(json.get("name"));
        }
    }
    //Creates an Arraylist<String> based on a jsonobject and a key
    private static ArrayList<String> createJsonStringList(JSONObject json, String key){
        JSONArray jsonArray = json.getJSONArray(key);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
    //Returns a list of books by the publisher
    private static ArrayList<JSONObject> booksPerPublisher(ArrayList<JSONObject> books, String publisher){
        ArrayList<JSONObject> publisherBooks = new ArrayList<JSONObject>();
        for (JSONObject book: books) {
            if(book.get("publisher").equals(publisher)){
                publisherBooks.add(book);
            }
        }
        return publisherBooks;
    }
    //Prints the pov characters per book
    private static void printPovCharactersPerBook(String publisher, ArrayList<JSONObject> publishersBooks){

        System.out.println("Pov characters per book published by " + publisher + ": ");
        System.out.println("-------------------------------------------------------");
        for (JSONObject publishersBook: publishersBooks) {
            System.out.println("Book: " + publishersBook.get("name"));
            System.out.println();

            ArrayList<String> povCharacters = createJsonStringList(publishersBook, "povCharacters");

            for (String povCharacter: povCharacters) {
                JSONObject jsonChar = apiCall.getObject(povCharacter);
                System.out.println(jsonChar.get("name"));
            }
            System.out.println("-------------------------------------------------------");
        }
    }
    private static ArrayList<JSONObject> getBooks(JSONArray jsonBooks){
        ArrayList<JSONObject> books = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonBooks.length(); i++) {
            books.add(jsonBooks.getJSONObject(i));
        }
        return books;
    }
}
