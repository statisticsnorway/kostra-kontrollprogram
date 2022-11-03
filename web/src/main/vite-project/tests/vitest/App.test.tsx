/**
 * @jest-environment jsdom
 */
import App from '../../src/App'
import {render, screen, fireEvent} from '@testing-library/react'
// note: you can make these globally available from vite.config.js
// see the Vitest docs for more: https://vitest.dev/config/#globals
import {beforeAll, describe, expect, it} from 'vitest'

describe('App', () => {
    beforeAll(() => {
        render(<App/>)
    });

    it("count is zero when page is loaded", async () => {
        expect(screen.getByText(/count is 0/i)).toBeDefined()
    })

    it("count is increased by one when button is clicked", async () => {
        const countButton = screen.getByRole("button")
        fireEvent.click(countButton)
        expect(screen.getByText(/count is 1/i)).toBeDefined()
    })
})