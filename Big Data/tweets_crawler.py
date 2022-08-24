import tweepy
import pymongo
from pymongo import MongoClient
import requests

bearer_token = ''
# create clients
t_client = tweepy.Client(bearer_token, return_type=requests.Response, wait_on_rate_limit=True)
m_client = MongoClient('localhost', 27017)
db = m_client['twitter_db']

# set queries and fields to retrieve
query = ['Elon','Musk','TWTR']
fields = ['text','author_id','id','created_at']

# Set end date to search tweets up to
# must be within 7 days before today
# end_date = '2022-04-14T00:00:00Z'

# for day in range(14, 20):
#     end_date = f'2022-04-{day}T00:00:00Z'

# search for each query term
for word in query:
    index = 0
    # create collection for each different term
    term = 'twitter_' + word
    collection = db[term]

    # get tweet count in collection and set desired tweet count
    index = collection.count_documents({})
    max_tweets = 600

    # collect up to max tweet count
    while index <= max_tweets:
        try:
            # ensure each tweet is unique in collection
            collection.create_index([("id", pymongo.ASCENDING)], unique = True) 

            # search twitter api for term
            response = t_client.search_recent_tweets(word, max_results=100, tweet_fields=fields)

            # get json of response
            tweets_dict = response.json()
            tweets_data = tweets_dict['data']

            # insert each tweet into MongoDB
            for tweet in tweets_data:
                # print(str(index), end="")
                # print(tweet)
                # print("")

                # if id is unique, insert and update tweet count
                try:
                    collection.insert_one(tweet)
                    index += 1
                    print("tweet collected " + str(index))
                except:
                    pass
        except tweepy.TweepyException as e:
            print(str(e))
            break