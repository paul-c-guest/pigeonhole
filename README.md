# PigeonHole
## A small collection of tools developed to enable efficient photo sorting and analysis. 

**The motivation for these tools were:**
- eliminate the strain of repetitive mouse actions
- reduce mistakes at the analysis step

**Beneficial side-effects include:**
- fullscreen image assists determining sorting location
- noticing behaviour patterns
- familiarisation with regular visitors

## Tools available
### **Filter**
Manually sort a folder of images to pre-designated subfolders. User is presented an image, a valid keypress moves the file; the next image is shown.
### **Analyse**
Images in a folder are automatically sorted into clusters based on the times each image was taken. As of June 2022, the threshold for creating a new cluster is 300 seconds (5 minutes).

## Installation
Download PigeonHole.jar. 

---
### Requirements for compilation
[Metadata Extractor](https://github.com/drewnoakes/metadata-extractor)

[XPMCore](https://search.maven.org/artifact/com.adobe.xmp/xmpcore/6.1.11/bundle)