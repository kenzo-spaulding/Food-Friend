package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

// Downloads the image from the url
class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bitMap;

    public DownloadImage(ImageView bitMap){
        this.bitMap = bitMap;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urls_to_display = urls[0];
        Bitmap connect = null;
        try{
            InputStream in = new java.net.URL(urls_to_display).openStream();
            connect = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    protected void onPostExecute(Bitmap map){
        bitMap.setImageBitmap(map);
    }
}

//Item content organized
class Contact {
    private String logo_url = "https://logo.clearbit.com/innout.com";
    private String foodName;
    private String foodSize;
    private String foodMods;
    private String drinkName;
    private String drinkSize;
    private String drinkMods;

    public ArrayList<String> companyLogo = new ArrayList<>(Arrays.asList(
            "https://logo.clearbit.com/innout.com",
            "https://logo.clearbit.com/.com"
    ));

    public Contact(String logo_url, String foodName, String foodSize, String foodMods, String drinkName, String drinkSize, String drinkMods) {
        this.logo_url = logo_url;
        this.foodName = foodName;
        this.foodSize = foodSize;
        this.foodMods = foodMods;
        this.drinkName = drinkName;
        this.drinkSize = drinkSize;
        this.drinkMods = drinkMods;
    }

    public Contact(String foodName) {
        this.foodName = foodName;
    }

    public Contact(String foodName, String drinkName) {
        this.foodName = foodName;
        this.drinkName = drinkName;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getLogoURL(){ return logo_url; }

    public static ArrayList<Contact> createContactsList() {
        ArrayList<String> mylist = new ArrayList<>(Arrays.asList(
                "Parallel 37",
                "Starbelly",
                "Brass Tacks",
                "Lord Stanley",
                "Top of the Mark",
                "Pier 23",
                "Plumed Horse",
                "Everest",
                "The Aviary",
                "Rebar",
                "Proxi",
                "The Rosebud",
                "The Signature Room",
                "Goosefoot",
                "Catch 35",
                "Gemini ",
                "Blue Plate",
                "Bones",
                "Burger & Beer Joint",
                "Catch of the Day",
                "CRUST",
                "Daily Grill",
                "Full Moon",
                "Garage Kitchen + Bar",
                "Grubstake",
                "Harvest Beat",
                "Hot & Crusty",
                "King and Queen",
                "Lazy Bear",
                "Mad for Chicken",
                "Nightbird",
                "Outlier",
                "Rusty Pelican",
                "Sea Spice",
                "Stateside",
                "The Capital Grille",
                "The Deck",
                "The Local Eatery",
                "The Red Door",
                "Tower 23 Hotel",
                "Waterbar Restaurant",
                "Wise Sons ",
                "5 Spot",
                "A Salt & Battery",
                "Brewed Awakening",
                "Chart House",
                "Crest Cafe",
                "Double Decker",
                "EPIC Steak",
                "Foxsister",
                "Golden Era",
                "Halls Chophouse",
                "Heirloom Cafe",
                "Lattetude",
                "Level III Restaurant",
                "Mama’s Fish House",
                "Nin Com Soup",
                "Ozone",
                "Rooftoop at E11EVEN",
                "Skillet Counter",
                "Stoneburner",
                "Street Taco",
                "The Egg & Us",
                "The Incredible Cafe",
                "The River Seafood",
                "Townsend",
                "Upland",
                "Urban Remedy",
                "Zero Restaurant ",
                "750 Restaurant",
                "Bean Around the World Coffees",
                "Blackbrick",
                "Cafe Coyote",
                "Chewy Balls",
                "Crab Hut",
                "Double Knot",
                "Famous Lunch",
                "Fog Harbor Fish House",
                "Gaslamp Fish House",
                "Hereford Grill",
                "Hungry’s Kitchen & Tap",
                "King Lee’s",
                "Like No Udder",
                "Marina Kitchen",
                "Munch Box",
                "My Dung",
                "North Beach Restaurant",
                "Opera Cafe",
                "Queenstown Public House",
                "Rocco’s Cafe",
                "Six Seven Restaurant",
                "Stomach Clinic Railways Restaurant",
                "The Fishery",
                "The Local House",
                "The Table at Season To Taste",
                "True Food Kitchen",
                "Zero Zero ",
                "Award Wieners",
                "Beaver Choice",
                "Blue Collar",
                "Butty Boys",
                "California Pizza Kitchen",
                "Chez Billy Sud",
                "Conch it Up Soul Food",
                "Filled of Dreams",
                "Fishing With Dynamite",
                "Golden Greek",
                "Heritage Restaurant & Caviar Bar",
                "Indigo Grill",
                "Kinship",
                "Le Parfait",
                "Little Star Pizza",
                "Market Restaurant and Bar",
                "Momofuku Ko",
                "Old Lisbon",
                "Owen & Engine",
                "Pita Pan",
                "Quince",
                "Sears Fine Food",
                "The Big 4",
                "The Bear & The Monarch",
                "The Marine Room",
                "The Patio",
                "The Smoking Goat",
                "Top of the Market",
                "Zuma ",
                "13 Coins",
                "Basic Kneads Pizza",
                "Burger Lounge",
                "Chops & Hops",
                "Dragon Eats",
                "Eight AM",
                "En Thai Sing",
                "Fiddler’s Green",
                "Girl & the Goat",
                "Jar + Fork",
                "Juan in a Million",
                "Lox Stock & Bagel",
                "Meat U There",
                "Old Town Mexican Cafe",
                "One Market Restaurant",
                "Pig’N Pancake",
                "Planet of the Grapes",
                "PM Fish & Steak House",
                "Project Juice",
                "Shaker + Spear",
                "Smoque BBQ",
                "Soon Fatt",
                "Thai Tanic",
                "The Chocolate Log",
                "The Lost Kitchen",
                "The Pink Door",
                "The Purple Pig",
                "Three…",
                "Toro Toro Restaurant ",
                "3 Way Restaurant",
                "Bite Me Sandwiches",
                "Blue Mermaid",
                "Burma Love",
                "Cat Heads BBQ",
                "Cutters Crabhouse",
                "Eatmore Fried Chicken",
                "Fishcotheque",
                "Great Eastern Restaurant",
                "Hillstone",
                "Island Prime",
                "Lettuce Eat",
                "Little Sheep",
                "Lord of the Fries",
                "Metro Cafe",
                "Ocean Star",
                "Oasis Fried Chicken",
                "Party Fowl",
                "Peking Inn",
                "Phat Phuc Noodle Bar",
                "Pu Pu Hot Pot",
                "Shuckers Restaurant",
                "Single Shot",
                "Staple & Fancy",
                "Thai Me Up",
                "Thai The Knot",
                "The Chef in the Hat",
                "The Golden Stool",
                "The Poke Co.",
                "The Slanted Door",
                "Wok This Way ",
                "Bankers Hill",
                "Barefoot Bar & Grill",
                "Bask",
                "Cloak & Petal",
                "Corridor",
                "Duke’s Seafood",
                "Emerald Grill",
                "Fog City",
                "Green Leaf",
                "Harbor House Restaurant",
                "Hollywood Cafe",
                "Just Falafs",
                "Lionfish",
                "Maza Grill Kabob",
                "Mister A’s",
                "Palomino",
                "Poke Life",
                "Rich Table",
                "Season 53",
                "Smyth",
                "Spinasse",
                "The French Gourmet",
                "The Polo Bar",
                "The View Lounge",
                "Umami Burger",
                "Vessel Restaurant",
                "Zoës Kitchen ",
                "BJ’s Restaurant Brewhouse",
                "BRIO Tuscan Grille",
                "Cafe 21",
                "City View Restaurant",
                "Cosmopolitan Restaurant",
                "Crabby Dick’s",
                "Fare Start",
                "Greens",
                "Gringos Locos",
                "Harbor City Restaurant",
                "House of Blues",
                "La Grotta",
                "Metropolitan Grill",
                "Rainforest Cafe",
                "Rice House",
                "Seaview Restaurant",
                "Spruce",
                "The House",
                "The Inn",
                "The Kitchen",
                "The Melting Pot",
                "The Mission",
                "The Saddle River Inn",
                "Tin Roof",
                "Water Grill",
                "Alinea",
                "Barley Mash",
                "Blunch",
                "Boka",
                "Cafe Provence",
                "Coaster Saloon",
                "Coppa",
                "Firefly Restaurant",
                "Geronimo",
                "Goldfinch Tavern",
                "Homestyle Hawaiian",
                "Kum Den Bar & Restaurant",
                "Maude",
                "Next",
                "Pinch Kitchen",
                "Revelry Bistro",
                "Seven Hills",
                "Sotto",
                "SPIN",
                "Tanta",
                "The Grove",
                "The Old Spaghetti Factory",
                "Toscana",
                "Waterfront Restaurant",
                "World Famous"
        ));
        ArrayList<Contact> contacts = new ArrayList<>();

        for (int i = 1; i < mylist.size(); i++) {
            contacts.add(new Contact(mylist.get(i)));
        }
        return contacts;
    }

    public void onClick(View view) {

    }

}


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /* need to do this everywhere we refer the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public void deleteItem(){
        StoreItem s (StoreItem)list.get(position);
        String key = s.getKey();
        myRef.child(key).removeValue();
        list.remove(position);
        notifyDataSetChanged();
    }
    *///end of database code


    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        protected ImageView imageView_logo;
        protected TextView textView_FoodName;
        protected TextView textView_FoodSize;
        protected TextView textView_FoodMods;
        protected TextView textView_DrinkName;
        protected TextView textView_DrinkSize;
        protected TextView textView_DrinkMods;

        // We also create a constructor that accepts the entire item_swipe row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageView_logo = (ImageView) itemView.findViewById(R.id.imageview_CompanyLogo);
            textView_FoodName = (TextView) itemView.findViewById(R.id.textView_FoodName);
            textView_FoodSize = (TextView) itemView.findViewById(R.id.textView_FoodSize);
            textView_FoodMods = (TextView) itemView.findViewById(R.id.textView_FoodMods);
            textView_DrinkName = (TextView) itemView.findViewById(R.id.textView_DrinkName);
            textView_DrinkSize = (TextView) itemView.findViewById(R.id.textView_DrinkSize);
            textView_DrinkMods = (TextView) itemView.findViewById(R.id.textView_FoodMods);
        }
    }

    // Store a member variable for the contacts
    private List<Contact> mContacts;

    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_content, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item_swipe through holder
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Contact contact = mContacts.get(position);

        // Set item_swipe views based on your views and data model
        viewHolder.textView_FoodName.setText(contact.getFoodName());
        new DownloadImage((ImageView) viewHolder.imageView_logo).execute(contact.getLogoURL());

        //Button button = viewHolder.messageButton;
        //button.setText(contact.isOnline() ? "Visit" : "Closed");
        //button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}


public class RecyclerViewActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;

    // Firebase variables
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        startListView();

        // connecting list to firebase NOTE: scroll down and click on real time database & select test mode
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete((ItemViewAdapter) adapter));
        //itemTouchHelper.attachToRecyclerView(recyclerView);

        //Intent intent = new Intent(this, SwipingActivity.class);
        //Pair<RecyclerViewActivity, Intent> pair = new Pair<>(this, intent);
        //SleepTimer.delay(5, pair);


        // Firebase: must do these in order
        //database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("Items"); // gets the branch called Item from your DB
        // note if it doesn't exist, Firebase will create it.
        // Your database needs a unique key, else it will be overrided
        //String key = myRef.push().getKey(); // generates a random key for us
        //myRef.child(key).setValue("My db has this as a value");
        // on friday we will go over updating the recycler view

    }

    private void startListView(){
        // ...
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList();
        // Create adapter passing in the sample user data
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
