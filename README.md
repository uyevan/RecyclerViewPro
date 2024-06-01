# RecyclerViewPro

[中文文档](https://github.com/uyevan/RecyclerViewPro/blob/master/README.CN.md)

## Project Introduction

This project is called RecyclerViewPro, and it's like putting a set of superhero equipment on the
list feature of your Android app. Imagine you have an app with a lot of information to display, just
like a thick phone book, but you want users to browse it quickly and conveniently, and be able to
view the latest content at any time. RecyclerViewPro is here to fulfill that wish.

It combines RecyclerView with SmartRefreshLayout to implement advanced features, including
pull-to-refresh, load-more, asynchronous requests, and dynamic data updates, and more.

## Project Highlights

- Pull-to-refresh: Just as easy as turning on a tap to drink water, users can simply pull down the
  list to see the latest information flowing in, making it perfect for news apps or social media,
  ensuring that the content is always fresh.
- Load-more: Users reach the bottom of the list and want to see more? No problem, automatically load
  more content, just like a magician pulling rabbits out of a hat, ensuring that the user's
  exploration journey never ends.
- Asynchronous requests: This means working quietly in the background for you. When users scroll the
  list, the data is already on its way, without causing lag due to loading, providing an
  ultra-smooth user experience.
- Dynamic updates: Data changed? No problem, the list will intelligently update itself, only
  changing the parts that need to be changed, highly efficient and not wasting resources.

## Related Dependencies

To use these cool features, you'll need to add some secret weapons - dependency libraries - to your
project, such as:

- Lombok: Saves you from writing a bunch of annoying getters and setters, making coding as smooth as
  writing a diary.
- Glide: A little helper for image loading, it can display images quickly no matter how large they
  are, and it also saves bandwidth.
- SmartRefreshLayout: The magic behind pull-to-refresh and load-more, and it comes with various
  beautiful animation effects.

```gradle
    // LomBok
    implementation(libs.org.projectlombok.lombok4)
    annotationProcessor(libs.org.projectlombok.lombok4)
    // Glide
    implementation(libs.glide)
    // Refresh
    implementation(libs.refresh.layout.kernel)
    implementation(libs.github.refresh.header.classics)
    implementation(libs.refresh.footer.classics)
```

## Check out the Effect

Do you want to witness the miracle with your own eyes? Here's a screenshot and a small video
demonstration, like a teaser trailer for the project, allowing you to experience the silky smooth
operation and instant updates in advance.

> Project Screenshot

<img alt="Demo" height="500" src="https://img2.imgtp.com/2024/06/01/SD4iyI04.png" width="1024"/>

> Run Demo

<img alt="Demo" height="500" src="https://img2.imgtp.com/2024/06/01/XRcuwOYn.gif" width="1024"/>

