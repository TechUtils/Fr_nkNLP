# Fr_nkNLP
**Fr_nkNLP** is a child project of the fr_nkenstien Project.
It is currently hosted on [Heroku](https://fr-nk-nlp.herokuapp.com/) as a free service.

This API converts Natural Language Sentences to JSON based tree structure identifying components such as Names, Organizations, Places, time, etc.

API is currently very basic and does not have much functionality.

Need to add:
- Ability to do optional Identification of Items
- Ability to train the models in Live Mode
- Incoming Message Logging capability
- Treebank Translations in Response

Backend: The code uses `Apache OpenNLP` Library

Endpoint URL: https://fr-nk-nlp.herokuapp.com/rest/ParseService/parse

Params:  (`POST`)
- UserID`(Field name: userid)`: Name/user id of the user who is saying the statement
- Sentence Text`(Field name: text)`: Sentence to be parsed.

> due to free service hosting limitations, the first responses may be slow and may time out. It takes time to restart the Dyno, therefore please wait for response.

> Only English language is supported at the moment