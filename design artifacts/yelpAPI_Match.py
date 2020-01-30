#Business Match       URL -- 'https://api.yelp.com/v3/businesses/matches'
# Link :  https://www.yelp.com/developers/documentation/v3/business_match

# Import the modules
import requests
import json

# Define a business ID
business_id = 'DDHQZBvNAcggUBBYkDDitA'                                   # previous Busness_ID  [ '4AErMBEoNzbk7Q8g45kKaQ' ] 
unix_time   = 1546047836

# Define API_Key 
API_KEY = '9sChg8nmLtdalv3Ls2uQVcnnThZCwPhGHSSfkn-0HKST6ksDZmzfn55yV1VBKG32TGE406Y-EAP3wC-h3aqZ7o6Qzsb7-X37piqoLItl0yrEXl8DBcI6I7BcS9EnXnYx'

# Define Endpoint
ENDPOINT = 'https://api.yelp.com/v3/businesses/matches'                  # Actual search that we want to perform in the Yelp 
                                                                        # Yelp Endpoint https://api.yelp.com/v3/PATH 
                                                                        #   PATH 
                                                                        #       Business Search : /businesses/search
                                                                        #       See more PATh at ( https://www.yelp.com/developers/documentation/v3/get_started )
# Define   Header
HEADERS = {'Authorization': 'bearer %s' % API_KEY}                      # HEADERS : Authorize ourseleves 
                                                                        #   Format is dictionary 
                                                                        #       { 'Key Value' : 'string value' % API_Key value }                        

# Define my parameters of the search


#BUSINESS MATCH PARAMETERS - EXAMPLE                                    
PARAMETERS = {'name': 'Starbucks',
             'address1': '18100 Culver Dr',
             'city': 'Irvine',
             'state': 'CA',
             'country': 'US'}

# Make a request to the Yelp API
response = requests.get(url = ENDPOINT,                                   #response is a variable from Yelp API(request) 
                        params = PARAMETERS,
                        headers = HEADERS)

# Conver the JSON String into dictionary(business_data)
business_data = response.json()

# print the response
print(json.dumps(business_data, indent = 3))  





