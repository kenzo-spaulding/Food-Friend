package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.foodie_friend.frontend.dependencies.SleepTimer;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class ContactsAdapter extends
        RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button messageButton;

        // We also create a constructor that accepts the entire item_swipe row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }

    // Store a member variable for the contacts
    private List<Contact> mContacts;

    // Pass in the contact array into the constructor
    public ContactsAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ContactsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Contact contact = mContacts.get(position);

        // Set item_swipe views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());
        Button button = viewHolder.messageButton;
        button.setText(contact.isOnline() ? "Visit" : "Closed");
        button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}

class Contact {
    private String mName;
    private boolean mOnline;

    public Contact(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;


    public static ArrayList<Contact> createContactsList(int numContacts) {
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
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i < mylist.size(); i++) {
            contacts.add(new Contact(mylist.get(i), i <= numContacts / 2));
        }

        return contacts;
    }

    public void onClick(View view) {

    }

}


public class RecyclerViewActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        startListView();



        Intent intent = new Intent(this, SwipingActivity.class);
        Pair<RecyclerViewActivity, Intent> pair = new Pair<>(this, intent);
        SleepTimer.delay(pair);
    }

    private void startListView(){
        // ...
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
