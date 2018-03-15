# Computer Vision

## Rationale

This game is particularly well fitted for the usage of computer vision. the most common use cases are to detect glyph, jewel color, and the cryptobox. This could give a very reliable way of detecting various objects, and thus warranted investigation.

## Implementation/Testing

In our testing of computer vision, we used a pixycam. We first tried this in an attempt to identify glyph colors, which is mentioned in further detail in Glyph Detection.

Our testing for the glyphs was mixed. On the first attempt, we naively used the pixycam, simply training it on the color right in front of the pixycam. This led to very poor results, with a very high rate of false positives.

Insert pic set 1

This did not work very well, as can be seen in the images. Speciically, there were many times where the identified box was much larger than the actual glyph, as well as indenfitied boxes which did not correspond to a glyph at all. 

The most succesful was the brown glyph image, in which the glyphs stacked on top of the pile were clearly identified, as well as some glyphs in the far background already in a cryptobox. However, the closeset one had large error, with even the identification box encompasing a gray glyph, in addition to the brown.

We also attempted to train it on some other objects, as the jewels and the relic. These were much more succesful than the glyph attempts, as they had much more contrast with the surrounings. 

Insert pic set 2

We then tried to identify glyph colors using pixycam again, with more knowledge about how to train the pixycam. The results are in the following images

Insert pic set 3

As can be seen in the images, the glyph detection worked much better. This was accomplished by manually selecting the box in which the pixycam was to train on, as well as adjusting the threshold that the pixycam required to mark a pixel as part of the object. Additionally, the entire feed was darkened. This increased the contrast between the closely placed glyphs, and the further away background, reducing false positives. 

Unfortunately, the setup was not FTC game legal.

Insert pic set 3_7

Both of the lights used are very bright, and would be very distracting to opposing and allied drivers. This is not something we would want on our bot.

Attempting to train and get similar results without the lights were largely unsuccesful, though better than the first attempt. There were too many false positives to make it a reliable system.

In addition, the lighting and backgrounds would change between the testing location and competition; a background at competition could result in false positives if it was gray or brown, and require retraining of the pixy, if it could still even work afterwards.

## Results

Ultiamtely, the usage of computer vision to detect glyphs using a pixycam were not succesful. However, using the pixycam to detect other objects worked well, though we had already created alternatives to the jewel detection, and relic identification can be done by driver's during teleop mode.
