import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import DictionaryPage from "../pages/DictionaryPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/dictionary" element={<DictionaryPage />} />
            </Routes>
        </Router>
    );
}

export default App;
