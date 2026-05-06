import { useEffect, useState } from 'react';

function App() {
    // --- STAVY (STATE) ---
    const [books, setBooks] = useState([]);
    const [allUsers, setAllUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [userSearchTerm, setUserSearchTerm] = useState('');

    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [publishYear, setPublishYear] = useState('');

    const [user, setUser] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);
    const [loginUsername, setLoginUsername] = useState('');
    const [loginPassword, setLoginPassword] = useState('');

    const [myLoans, setMyLoans] = useState([]);

    const [regData, setRegData] = useState({
        username: '',
        password: '',
        email: '',
        firstName: '',
        lastName: '',
        age: ''
    });

    // --- POMOCNÉ FUNKCE ---
    const getAuthorName = (authorData) => {
        if (!authorData) return "Neznámý";
        if (Array.isArray(authorData)) {
            return authorData.map(a => a?.name || "Neznámý").join(', ');
        }
        return (typeof authorData === 'object' ? authorData.name : authorData) || "Neznámý";
    };

    // --- API VOLÁNÍ ---
    const fetchBooks = () => {
        fetch('http://localhost:8080/api/books')
            .then(async response => {
                if (!response.ok) {
                    console.error("Backend vrátil chybu pro knihy:", response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log("Data z API knih:", data);
                setBooks(Array.isArray(data) ? data : []);
            })
            .catch(error => console.error("Chyba při načítání knih z API:", error));
    };

    const fetchAllUsers = (authData) => {
        const credentials = authData || user?.auth;
        if (!credentials) return;

        fetch('http://localhost:8080/api/test/users', {
            headers: { 'Authorization': `Basic ${credentials}` }
        })
            .then(async res => {
                if (!res.ok) {
                    console.error("Backend vrátil chybu pro uživatele:", res.status);
                }
                return res.json();
            })
            .then(data => {
                console.log("Data z API uživatelů:", data);
                setAllUsers(Array.isArray(data) ? data : []);
            })
            .catch(err => console.error("Chyba při načítání uživatelů z API:", err));
    };

    const handleGenerateData = () => {
        fetch('http://localhost:8080/api/test/generate', {
            method: 'POST',
            headers: { 'Authorization': `Basic ${user.auth}` }
        })
            .then(res => {
                if(res.ok) {
                    alert("Data byla úspěšně vygenerována!");
                    fetchAllUsers();
                    fetchBooks();
                } else {
                    alert("Chyba při generování dat na serveru. Zkontrolujte logy v IntelliJ.");
                }
            })
            .catch(err => alert("Chyba při spojení se serverem."));
    };

    const fetchMyLoans = (authData) => {
        const credentials = authData || user?.auth;
        if (!credentials) return;
        fetch('http://localhost:8080/api/loans/my-loans', {
            headers: { 'Authorization': `Basic ${credentials}` }
        })
            .then(res => res.json())
            .then(data => setMyLoans(Array.isArray(data) ? data : []))
            .catch(err => console.error("Chyba při načítání výpůjček:", err));
    };

    useEffect(() => {
        fetchBooks();
    }, []);

    useEffect(() => {
        if (user?.username === 'admin') {
            fetchAllUsers();
        }
    }, [user]);

    const handleRegister = (e) => {
        e.preventDefault();
        const { username, password, email, firstName, lastName, age } = regData;
        if (!username || !password || !email || !firstName || !lastName || !age) {
            alert("Všechna pole musí být vyplněna!");
            return;
        }

        fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ...regData, age: parseInt(age) })
        })
            .then(res => res.text())
            .then(msg => {
                alert(msg);
                if (msg.toLowerCase().includes("úspěšně")) {
                    setIsRegistering(false);
                    setRegData({ username: '', password: '', email: '', firstName: '', lastName: '', age: '' });
                }
            });
    };

    const handleLogin = (e) => {
        e.preventDefault();
        const cleanUsername = loginUsername.trim();
        const cleanPassword = loginPassword.trim();
        const credentials = btoa(`${cleanUsername}:${cleanPassword}`);

        fetch('http://localhost:8080/api/loans/my-loans', {
            headers: { 'Authorization': `Basic ${credentials}` }
        })
            .then(res => {
                if (res.ok) {
                    setUser({ username: cleanUsername, auth: credentials });
                    setLoginPassword('');
                    if (cleanUsername !== 'admin') fetchMyLoans(credentials);
                } else {
                    alert("Špatné jméno nebo heslo!");
                }
            })
            .catch(() => alert("Nepodařilo se spojit se serverem."));
    };

    const handleLogout = () => {
        setUser(null);
        setMyLoans([]);
        setAllUsers([]);
        setLoginUsername('');
        setLoginPassword('');
    };

    const handleAddBook = (e) => {
        e.preventDefault();
        const newBook = { title, author, publishYear: parseInt(publishYear) };

        fetch('http://localhost:8080/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Basic ${user.auth}`
            },
            body: JSON.stringify(newBook)
        })
            .then(res => {
                if (res.ok) {
                    alert("Kniha uložena!");
                    fetchBooks();
                    setTitle(''); setAuthor(''); setPublishYear('');
                } else {
                    alert("Chyba při ukládání.");
                }
            });
    };

    const handleDeleteBook = (id) => {
        if (!window.confirm("Opravdu smazat knihu?")) return;
        fetch(`http://localhost:8080/api/books/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Basic ${user.auth}` }
        })
            .then(res => { if (res.ok) fetchBooks(); });
    };

    const handleLoanBook = (bookId) => {
        fetch(`http://localhost:8080/api/loans/${bookId}`, {
            method: 'POST',
            headers: { 'Authorization': `Basic ${user.auth}` }
        })
            .then(res => {
                if (res.ok) {
                    alert("Kniha vypůjčena!");
                    fetchMyLoans();
                    fetchBooks(); // NAČTE KNIHY ZNOVU, ABY SE AKTUALIZOVALA DOSTUPNOST
                }
                else { alert("Tuto knihu nelze půjčit."); }
            });
    };

    const handleReturnBook = (loanId) => {
        if (!window.confirm("Opravdu chcete tuto knihu vrátit?")) return;

        fetch(`http://localhost:8080/api/loans/${loanId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Basic ${user.auth}` }
        })
            .then(res => {
                if (res.ok) {
                    alert("Kniha byla úspěšně vrácena!");
                    fetchMyLoans();
                    fetchBooks();
                } else {
                    alert("Chyba při vracení knihy.");
                }
            })
            .catch(() => alert("Nepodařilo se spojit se serverem."));
    };

    const filteredUsers = allUsers.filter(u => {
        const name = ((u.firstName || "") + " " + (u.lastName || "")).toLowerCase();
        const email = (u.email || "").toLowerCase();
        const dept = (u.department?.name || "").toLowerCase();
        const s = userSearchTerm.toLowerCase();
        return name.includes(s) || email.includes(s) || dept.includes(s);
    });

    const filteredBooks = books.filter(book => {
        const t = (book.title || "").toLowerCase();
        const a = (book.author || "").toLowerCase();
        const s = searchTerm.toLowerCase();
        return t.includes(s) || a.includes(s);
    });

    return (
        <div style={{ padding: '20px', fontFamily: 'Segoe UI, sans-serif', maxWidth: '1200px', margin: '0 auto', color: '#333' }}>

            <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px', padding: '20px', background: '#ffffff', borderRadius: '12px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>
                <h1 style={{ margin: 0, fontSize: '24px' }}>📚 Knihovní Systém</h1>
                <div>
                    {!user ? (
                        isRegistering ? (
                            <form onSubmit={handleRegister} style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', justifyContent: 'flex-end' }}>
                                <input type="text" placeholder="Jméno" required onChange={e => setRegData({...regData, firstName: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <input type="text" placeholder="Příjmení" required onChange={e => setRegData({...regData, lastName: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <input type="number" placeholder="Věk" required onChange={e => setRegData({...regData, age: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd', width: '60px'}} />
                                <input type="email" placeholder="Email" required onChange={e => setRegData({...regData, email: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <input type="text" placeholder="Login" required onChange={e => setRegData({...regData, username: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <input type="password" placeholder="Heslo" required onChange={e => setRegData({...regData, password: e.target.value})} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <button type="submit" style={{ background: '#28a745', color: 'white', border: 'none', padding: '8px 15px', borderRadius: '6px', cursor: 'pointer' }}>Registrovat</button>
                                <button type="button" onClick={() => setIsRegistering(false)} style={{ border: 'none', background: 'none', cursor: 'pointer', color: '#666', textDecoration: 'underline' }}>Zrušit</button>
                            </form>
                        ) : (
                            <form onSubmit={handleLogin} style={{ display: 'flex', gap: '8px' }}>
                                <input type="text" placeholder="Uživatelské jméno" required value={loginUsername} onChange={e => setLoginUsername(e.target.value)} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <input type="password" placeholder="Heslo" required value={loginPassword} onChange={e => setLoginPassword(e.target.value)} style={{padding: '8px', borderRadius: '4px', border: '1px solid #ddd'}} />
                                <button type="submit" style={{ background: '#007bff', color: 'white', border: 'none', padding: '8px 15px', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }}>Přihlásit</button>
                                <button type="button" onClick={() => setIsRegistering(true)} style={{ background: '#6c757d', color: 'white', border: 'none', padding: '8px 15px', borderRadius: '6px', cursor: 'pointer' }}>Registrace</button>
                            </form>
                        )
                    ) : (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                            <span>Vítejte, <strong style={{color: '#007bff'}}>{user.username}</strong></span>
                            <button onClick={handleLogout} style={{ padding: '8px 15px', cursor: 'pointer', borderRadius: '6px', border: '1px solid #dc3545', color: '#dc3545', background: 'white' }}>Odhlásit se</button>
                        </div>
                    )}
                </div>
            </header>

            {user?.username === 'admin' && (
                <section style={{ marginBottom: '50px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <div>
                            <h2 style={{ margin: 0 }}>Správa systému</h2>
                            <p style={{ margin: 0, color: '#666' }}>Ukázka vazeb 1:M (Oddělení) a M:N (Role)</p>
                        </div>
                        <button onClick={handleGenerateData} style={{ background: '#5c4ee5', color: 'white', border: 'none', padding: '12px 20px', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold' }}>⚡ Generovat data</button>
                    </div>

                    <input
                        type="text"
                        placeholder="🔍 Hledat uživatele..."
                        value={userSearchTerm}
                        onChange={e => setUserSearchTerm(e.target.value)}
                        style={{ width: '100%', padding: '15px', marginBottom: '20px', borderRadius: '8px', border: '1px solid #eee', background: '#f8f9fa' }}
                    />

                    <table style={{ width: '100%', borderCollapse: 'collapse', background: 'white', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
                        <thead>
                        <tr style={{ textAlign: 'left', borderBottom: '2px solid #eee' }}>
                            <th style={{ padding: '15px' }}>UŽIVATEL</th>
                            <th style={{ padding: '15px' }}>ODDĚLENÍ</th>
                            <th style={{ padding: '15px' }}>ROLE</th>
                            <th style={{ padding: '15px' }}>ID</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredUsers.length > 0 ? (
                            filteredUsers.map(u => (
                                <tr key={u.id} style={{ borderBottom: '1px solid #f8f9fa' }}>
                                    <td style={{ padding: '15px' }}>
                                        <div style={{ fontWeight: 'bold' }}>{u.firstName} {u.lastName} ({u.age})</div>
                                        <div style={{ fontSize: '12px', color: '#888' }}>{u.email}</div>
                                    </td>
                                    <td style={{ padding: '15px' }}>{u.department?.name || "---"}</td>
                                    <td style={{ padding: '15px' }}>{u.roles?.map(r => r.name).join(', ')}</td>
                                    <td style={{ padding: '15px', fontSize: '10px', color: '#ccc' }}>{u.id}</td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="4" style={{ padding: '20px', textAlign: 'center', color: '#888' }}>Žádná data k zobrazení. Klikněte na "Generovat data".</td></tr>
                        )}
                        </tbody>
                    </table>
                </section>
            )}

            <section>
                <h2 style={{ marginBottom: '20px' }}>Katalog knih</h2>
                {user?.username === 'admin' && (
                    <div style={{ marginBottom: '30px', padding: '20px', border: '2px solid #28a745', borderRadius: '12px', background: '#f8fff8' }}>
                        <form onSubmit={handleAddBook} style={{ display: 'flex', gap: '15px' }}>
                            <input type="text" placeholder="Název knihy" required value={title} onChange={e => setTitle(e.target.value)} style={{ flex: 2, padding: '10px', borderRadius: '6px', border: '1px solid #ccc' }} />
                            <input type="text" placeholder="Autor" required value={author} onChange={e => setAuthor(e.target.value)} style={{ flex: 1, padding: '10px', borderRadius: '6px', border: '1px solid #ccc' }} />
                            <input type="number" placeholder="Rok" required value={publishYear} onChange={e => setPublishYear(e.target.value)} style={{ width: '100px', padding: '10px', borderRadius: '6px', border: '1px solid #ccc' }} />
                            <button type="submit" style={{ background: '#28a745', color: 'white', border: 'none', padding: '10px 20px', borderRadius: '6px', cursor: 'pointer' }}>ULOŽIT</button>
                        </form>
                    </div>
                )}

                <input
                    type="text"
                    placeholder="🔍 Hledat knihu..."
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                    style={{ width: '100%', padding: '15px', marginBottom: '20px', borderRadius: '8px', border: '1px solid #ccc' }}
                />

                <table style={{ width: '100%', borderCollapse: 'collapse', background: 'white', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>
                    <thead>
                    <tr style={{ background: '#343a40', color: 'white', textAlign: 'left' }}>
                        <th style={{ padding: '15px' }}>ID</th>
                        <th style={{ padding: '15px' }}>Název knihy</th>
                        <th style={{ padding: '15px' }}>Autor</th>
                        <th style={{ padding: '15px' }}>Rok vydání</th>
                        {/* NOVÝ SLOUPEC DOSTUPNOST */}
                        <th style={{ padding: '15px' }}>Dostupnost</th>
                        {user && <th style={{ padding: '15px' }}>Akce</th>}
                    </tr>
                    </thead>
                    <tbody>
                    {filteredBooks.length > 0 ? (
                        filteredBooks.map(book => (
                            <tr key={book.id} style={{ borderBottom: '1px solid #eee' }}>
                                <td style={{ padding: '15px' }}>{book.id}</td>
                                <td style={{ padding: '15px', fontWeight: 'bold' }}>{book.title}</td>
                                <td style={{ padding: '15px' }}>{book.author}</td>
                                <td style={{ padding: '15px' }}>{book.publishYear}</td>
                                {/* ZOBRAZENÍ STAVU DOSTUPNOSTI */}
                                <td style={{ padding: '15px' }}>
                                    {book.available ? (
                                        <span style={{ color: '#28a745', fontWeight: 'bold' }}>● Volná</span>
                                    ) : (
                                        <span style={{ color: '#dc3545', fontWeight: 'bold' }}>● Půjčená</span>
                                    )}
                                </td>
                                {user && (
                                    <td style={{ padding: '15px' }}>
                                        {user.username === 'admin' ? (
                                            <button onClick={() => handleDeleteBook(book.id)} style={{ background: '#dc3545', color: 'white', border: 'none', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer' }}>Smazat</button>
                                        ) : (
                                            /* TLAČÍTKO SE ZABLOKUJE, POKUD NENÍ KNIHA DOSTUPNÁ */
                                            <button
                                                disabled={!book.available}
                                                onClick={() => handleLoanBook(book.id)}
                                                style={{
                                                    background: book.available ? '#007bff' : '#ccc',
                                                    color: 'white',
                                                    border: 'none',
                                                    padding: '6px 12px',
                                                    borderRadius: '4px',
                                                    cursor: book.available ? 'pointer' : 'not-allowed'
                                                }}
                                            >
                                                {book.available ? "Půjčit" : "Půjčeno"}
                                            </button>
                                        )}
                                    </td>
                                )}
                            </tr>
                        ))
                    ) : (
                        <tr><td colSpan="6" style={{ padding: '20px', textAlign: 'center', color: '#888' }}>Žádné knihy k zobrazení. Data jsou prázdná nebo nastala chyba serveru.</td></tr>
                    )}
                    </tbody>
                </table>
            </section>

            {/* UPRAVENÁ SEKCE VÝPŮJČEK S TLAČÍTKEM VRÁTIT */}
            {user && user.username !== 'admin' && (
                <section style={{ marginTop: '50px', padding: '30px', backgroundColor: '#fff9db', borderRadius: '12px', border: '1px solid #fab005' }}>
                    <h2 style={{ marginTop: 0, color: '#f08c00', fontSize: '22px' }}>📖 Moje aktuální výpůjčky</h2>
                    {myLoans.length === 0 ? <p style={{color: '#666'}}>Nemáte žádné aktivní výpůjčky.</p> : (
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr style={{ textAlign: 'left', borderBottom: '2px solid #fab005' }}>
                                <th style={{ padding: '10px' }}>Název knihy</th>
                                <th style={{ padding: '10px' }}>Datum půjčení</th>
                                <th style={{ padding: '10px' }}>Akce</th>
                            </tr>
                            </thead>
                            <tbody>
                            {myLoans.map(loan => (
                                <tr key={loan.id}>
                                    <td style={{ padding: '10px' }}>{loan.book?.title}</td>
                                    <td style={{ padding: '10px' }}>{loan.loanDate ? new Date(loan.loanDate).toLocaleDateString('cs-CZ') : "---"}</td>
                                    <td style={{ padding: '10px' }}>
                                        <button
                                            onClick={() => handleReturnBook(loan.id)}
                                            style={{ background: '#ffc107', color: '#212529', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                                        >
                                            Vrátit
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </section>
            )}
        </div>
    );
}

export default App;