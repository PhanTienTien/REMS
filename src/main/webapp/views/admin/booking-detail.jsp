<h2>Booking Detail</h2>

<div>

    <p><b>ID:</b> ${booking.bookingId}</p>

    <p><b>Status:</b> ${booking.status}</p>

    <p><b>Created:</b> ${booking.createdAt}</p>

    <p><b>Accepted At:</b> ${booking.acceptedAt}</p>

</div>

<hr>

<h3>Property</h3>

<p><b>Title:</b> ${booking.propertyTitle}</p>
<p><b>Address:</b> ${booking.propertyAddress}</p>

<hr>

<h3>Customer</h3>

<p><b>Name:</b> ${booking.customerName}</p>
<p><b>Email:</b> ${booking.customerEmail}</p>

<hr>

<h3>Note</h3>

<p>${booking.note}</p>

<hr>

<h3>Processed By</h3>

<p>${booking.staffName}</p>