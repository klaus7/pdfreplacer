# PDF Search & Replace

This little library can be used to find text markers in PDF documents and add text blocks at the found locations. Placeholders in the original PDF are used to detect the location of the field, where the custom value can be added. Text dimensions are kept from the found marker text box

This library was written to automatically create filled-out agreement forms and similar documents. 

*This library is an addition to Apache's pdfbox.*

## Example

| From ...      | To ...      |
|------------|-------------|
| ![Alt text](/doc/test_manual.jpg) | ![Alt text](/doc/test_manual_processed.jpg) |

## Maven

```xml
<dependency>
    <groupId>com.allpiper</groupId>
    <artifactId>pdfreplacer</artifactId>
    <version>0.2.0</version>
</dependency>
```
