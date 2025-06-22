# Pokemon App

A Kotlin/Java project for listing Pokemon and also the detailed information about any selected Pokemon using clean architecture.

## Features
- Pokemon listing with pagination
- Pokemon details view with scrollable banners for images
- MVVM/Clean architecture
- Coroutines and Flow
- State management
- UI and Unit test cases for most important scenarios
- Retry functionality for API failures in both the screens

## Future Enhancements
1. We can introduce DataSources as the project scope grows.
2. Instead of using Result class that we generate on our own, we can use the one from Arrow library.
3. Loading can be handled in slightly better manner.

## Open Issues
- When we navigate to the detail screen, the content is loaded few times. There is an open issue which google is saying that it is an intended behaviour. It is happening as the recomposition is being triggered for navigation compasable which ideally should not happen. We have to dig deeper and understand the alternative implementation that can avoid this isse. Issue link https://issuetracker.google.com/issues/225987040?pli=1

## Recordings 
[Pokemon detail screen](https://github.com/user-attachments/assets/7dcc32ac-9daa-4773-855a-7a6e9e3c5e94)
[Pokemon list with simple pagination and loading](https://github.com/user-attachments/assets/a47b0b88-3e9b-4309-9173-45831e098087)
[Retry with error message](https://github.com/user-attachments/assets/1c1a2afe-74f0-48c6-8ea4-82fbdeb471af)

## License
MIT

