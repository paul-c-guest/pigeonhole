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

## Usage
_**Note: PigeonHole requires an existing Java installation of at least version 1.8**_

Download [PigeonHole.jar](https://github.com/paul-c-guest/pigeonhole/blob/main/PigeonHole.jar)

There are two main ways to run a JAR file:
- Some operating systems allow for the direct execution of JAR files. Try double-clicking the file, or right-click and find a relevant command 
- Execute the file from the command prompt: `java -jar PigeonHole.jar`

Select your target folder, then select the tool for the job.

## Keys
### **Filter**
- `P` - Parakeet
- `B` - Bird
- `E` - Empty / Nothing
- `M` - Mammal / Non-bird
- `F` - Foggy 
- `O` - Other / Human
- `Esc` - Stop and exit 

### **Analyse**
- `LEFT` / `RIGHT` arrow keys: move forward and back within the current cluster of images
- `SHIFT` + `LEFT` / `RIGHT`: move forward or back to the next cluster
- `Esc` - Stop and exit

----
### Requirements for compilation
[Metadata Extractor](https://github.com/drewnoakes/metadata-extractor)

[XPMCore](https://search.maven.org/artifact/com.adobe.xmp/xmpcore/6.1.11/bundle)
