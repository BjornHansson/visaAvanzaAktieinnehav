# visaAvanzaAktieinnehav
Läser in en nerladdad HTML-fil från en nätbank och visar alla aktieinnehaven i ett cirkeldiagram.

User story: Som användare vill jag kunna se alla mina innehav (från alla mina konton) i diagram, så att jag får en överblick av fördelningen.

![Screenshot](screenshot.jpg?raw=true "Screenshot")

## Starta
- Spara ner en fil med innehaven genom att kopiera HTML (via web console) från nätbanken.
- Sätt `BANK_HTML_FILE` som en användarvariabel innehållande  filens sökväg.
- Kör `mvn clean javafx:run`.