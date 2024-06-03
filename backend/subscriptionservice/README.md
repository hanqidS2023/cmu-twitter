# Subscription Service

# Usage
```
chmod +x build.sh
./build.sh
```

```
chmod +x run.sh
./run.sh
```

Then server would run on port 8082 by defult.


Certainly! Here's the API documentation formatted in Markdown:

SubscriptionController API Documentation
Base URL
Replace [Base URL] with the actual base URL of the API, which depends on the deployment environment.

# Endpoints
1. Get All Followers
Endpoint: ```[Base URL]/followers```
Method: ```GET```
Description: Retrieves a list of all followers for a specific user.
Request Body: GetSubscribersRequest object with userId field.
Response: List of User objects representing the followers.
2. Get All Mutual Followers
Endpoint: ```[Base URL]/followers/mutual```
Method: ```GET```
Description: Retrieves a list of all mutual followers for a specific user.
Request Body: GetSubscribersRequest object with userId field.
Response: List of User objects representing mutual followers.
3. Get Followers Count
Endpoint: ```[Base URL]/followers/coun```
Method: ```GET```
Description: Retrieves the count of followers for a specific user.
Request Body: GetSubscribersRequest object with userId field.
Response: Integer value indicating the number of followers.
4. Get All Subscriptions
Endpoint: ```[Base URL]/subscriptions```
Method: ```GET```
Description: Retrieves a list of all subscriptions for a specific user.
Request Body: GetSubscriptionsRequest object with userId field.
Response: List of User objects representing the subscriptions.
5. Get Subscriptions Count
Endpoint: ```[Base URL]/subscriptions/count```
Method:```GET```
Description: Retrieves the count of subscriptions for a specific user.
Request Body: GetSubscriptionsRequest object with userId field.
Response: Integer value indicating the number of subscriptions.
6. Add Subscription
Endpoint: ```[Base URL]/subscriptions```
Method: ```PUT```
Description: Adds a subscription for a user.
Request Body: SubscribeRequest object with userId and userIdSubscribeTo fields.
Response: No content (void).
7. Create User
Endpoint: ```[Base URL]/subscriptions```
Method: ```POST```
Description: Creates a new user with a name and userId.
Request Body: CreateUserRequest object with userId and username fields.
Response: User object representing the newly created user.
8. Remove Subscription
Endpoint: ```[Base URL]/subscriptions```
Method: ```DELETE```
Description: Removes a subscription from a user.
Request Body: UnsubscribeRequest object with userId and userIdAnother fields.
Response: No content (void).

