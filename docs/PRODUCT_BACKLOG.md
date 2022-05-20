# Product Backlog

## Minimum functionalities expected on the product (V.1.0)

- [x] 1. As a user, I should be able to start the application and see a welcome screen
- [x] 2. As a user, I must be able to set and change my screen name and my password
- [x] 3. As an administrator, I need to be able to add a room with details (size, beds, number, location and other information)
- [x] 4. As an administrator, I should be able to change the details of a room
- [x] 5. As an administrator, I can delete a room
- [x] 6. As an administrator, I can add a user as either reception staff or administrator
- [x] 7. As a reception staff, I must be able to see details of a room
- [x] 8. As a reception staff, I can see all the bookings for a specific day
- [x] 9. As a reception staff, I can see all the bookings for a specific room
- [x] 10. As a reception staff, I must be able to book a room for a specific date range
- [x] 11. As a reception staff, I can see an overview of the bookings
- [x] 12. As a reception staff, I will be able to change a booking
- [x] 13. As a reception staff, I can mark a room booking as paid
- [x] 14. As a reception staff, I can create a customer with data (name, address, payment method etc)
- [x] 15. As a reception staff, I can change details for a customer
- [x] 16. As a reception staff, I must be able to search for a booking
- [x] 17. As a reception staff, I can search for available free dates for rooms

Notice that several of the user stories can be implemented in several ways. The last one, for example, can be a straight search for a date and a room, or for several rooms in a range of dates.

# What's next? (what we would have liked to do)
- [ ] Improve / refine the design of the app (GUI)
- [ ] Improve the UI experience (create a search bar for selecting the customers when we do a new booking)
- [ ] Continue the javadoc
- [ ] Try to generalize the View classes: there is a lot of code redundancy in the `NewExampleView`, `DeleteExampleView` and `UpdateExampleView` views in the views package, it would just be enough to make an abstract `ExampleView` class for each `Example` and inherit the other views to access the parameters and implement the specific methods and avoid retyping the same code
- [ ] Improve / optimize the code of HotelProject (code redundancy + ghost code + the management of privileges between the different types of users: rstaff & admin)
- [ ] Improve and clarify the data management (dbm & hdata in the controllers package)
- [ ] Improve / optimize the error handling system (maybe create a special class for this)
- [ ] Generalize the tests (should work for any instances) & Improve the CI / pipeline stuffs (the different stages)
