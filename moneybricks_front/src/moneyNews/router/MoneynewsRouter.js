import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MoneynewsPage from "../page/MoneynewsPage";

function APP() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/moneynews" element={<MoneynewsPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default APP;