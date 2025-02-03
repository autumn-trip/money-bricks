import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MoneynewsPage from '../pages/moneynews/MoneynewsPage';

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