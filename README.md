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
Update: Resolved this issue using workaround of using LaunchedEffects in commit https://github.com/ArunSainiGA/PokemonProject/commit/d17e3d31a8904135bd17130d73f6175c0963b1b0 

## Recordings 
- [Pokemon Detail Screen.webm](https://github.com/user-attachments/assets/aaabf996-8969-401b-a003-4cb702d0faf2)
- [Pokemon list with simple pagination and loading](https://github.com/user-attachments/assets/a47b0b88-3e9b-4309-9173-45831e098087)
- [Retry with error message. Mocked the failure at first time to record the videoe](https://github.com/user-attachments/assets/1c1a2afe-74f0-48c6-8ea4-82fbdeb471af)

## License
MIT

