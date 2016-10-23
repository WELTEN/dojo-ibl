// test.js
describe('Protractor Demo App', function() {

    it('should have a title after login', function() {
        expect(browser.getTitle()).toEqual('DojoIBL | Inquiries view');
    });

    it('should create an inquiry', function() {
        element(by.id('add-inquiry-new')).click();

        element(by.model('game.title')).sendKeys("Inquiry Test");

        element(by.id('inquiry-new-create')).click();

        expect(element(by.css('.show-runs')).getText()).toBe('Inquiry Test');
    });

    //it('should add four and six', function() {
    //    // Fill this in.
    //    firstNumber.sendKeys(6);
    //    secondNumber.sendKeys(4);
    //
    //    goButton.click();
    //
    //    expect(latestResult.getText()).toEqual('10');
    //});
});