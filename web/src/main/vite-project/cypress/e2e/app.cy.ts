describe("app", () => {
    beforeEach(() => {
        cy.visit("/");
    });

    it("renders initial count", () => {
        const button = cy.findByRole("button", {name: /count is 0/i})
        button.should("exist");
    });

    it("count is incremented when button is clicked", () => {
        const button = cy.findByRole("button", {name: /count is 0/i})
        button.click()
        cy.findByRole("button", {name: /count is 1/i}).should("exist");
    });
});

export {}