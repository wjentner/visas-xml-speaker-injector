# xmlConversion 
The aim of this package is to convert text files into xml files. This is necessary to run the xml files in the Visas-Pipeline.
For this, the txt-files need to be in an appropriate format as described below. 

Source files:
   1. FileConverter
   2. XmlWriter
## Input 
The .txt files that shall be processed need to be stored in the input folder. 
### Format
The first and second line should be:
 
    Code: ...
    Essay [any number]: ...
    
Utterances will be grouped by the following identifiers: "\<new paragraph>" or "\<skips line>".

New Lines will be added by looking for the newLine-identifier "\<new line>".

Lines will be skipped, if there are identifiers, such as "\<bib>","\<keywords>", "\<begin footnote>" or "\<end notes>".

_Example_:

    Code: anyID
    Essay 1: essay title 
    <new paragraph>
    paragraph1
    <new line>
    Sentence starting in new line.
    <new paragraph>
    paragraph2
    <new line>
    Sentence starting in a new line.   
    <new line>
    Sentence starting in a new line.
    <bib>
    Everything written here will be ignored and will not be written into the xml file. 
## Output 
The processed xml files will be stored in the outputfolder directly.
# SpeakerEncoder
Optional code that can be executed to enable glyph aggregation based on "studentID", "semester", "year", "course Name", "course number", "essay number", "number of pages"  instead of only 'speaker'-aggregation. 
