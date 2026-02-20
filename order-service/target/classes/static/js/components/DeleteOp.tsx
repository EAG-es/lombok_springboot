import React from 'react';

const DeleteComponent: React.FC = () => {
    return (
        <div className="p-4 border border-danger rounded bg-light shadow-sm">
            <h4 className="text-danger mb-3">Delete Item</h4>
            <p className="text-muted">Enter the ID of the item you want to remove securely.</p>
            <div className="input-group mb-3">
                <input type="text" className="form-control border-danger" placeholder="Item ID..." />
                <button className="btn btn-danger">Confirm Delete</button>
            </div>
        </div>
    );
};

export default DeleteComponent;
